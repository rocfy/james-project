/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

package org.apache.james.backends.cassandra.utils;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.insertInto;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import org.apache.james.backends.cassandra.CassandraCluster;
import org.apache.james.backends.cassandra.components.CassandraModule;
import org.apache.james.backends.cassandra.components.CassandraTable;
import org.apache.james.backends.cassandra.components.CassandraType;
import org.apache.james.util.CompletableFutureUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.datastax.driver.core.DataType;
import com.datastax.driver.core.schemabuilder.SchemaBuilder;
import com.datastax.driver.core.utils.UUIDs;
import com.google.common.collect.ImmutableList;

public class PaggingTest {
    private static final String TABLE_NAME = "test";
    private static final String ID = "id";
    private static final String CLUSTERING = "clustering";
    private static final UUID UUID = UUIDs.timeBased();

    private CassandraCluster cassandra;
    private CassandraAsyncExecutor executor;

    @Before
    public void setUp() {
        cassandra = CassandraCluster.create(new CassandraModule() {
            @Override
            public List<CassandraTable> moduleTables() {
                return ImmutableList.of(new CassandraTable(TABLE_NAME,
                    SchemaBuilder.createTable(TABLE_NAME)
                        .ifNotExists()
                        .addPartitionKey(ID, DataType.timeuuid())
                        .addClusteringColumn(CLUSTERING, DataType.bigint())));
            }

            @Override
            public List<CassandraType> moduleTypes() {
                return ImmutableList.of();
            }
        });
        cassandra.ensureAllTables();
        executor = new CassandraAsyncExecutor(cassandra.getConf());
    }

    @After
    public void tearDown() {
        cassandra.clearAllTables();
    }

    @Test
    public void pagingShouldWork() {
        int fetchSize = 200;
        int size = 2 * fetchSize + 50;

        CompletableFutureUtil.allOf(
            IntStream.range(0, size)
                .boxed()
                .map(i ->
                    executor
                        .executeVoid(insertInto(TABLE_NAME)
                            .value(ID, UUID)
                            .value(CLUSTERING, i))))
            .join();

        assertThat(
            executor.execute(select()
                .from(TABLE_NAME)
                .where(eq(ID, UUID))
                .setFetchSize(fetchSize))
                .join())
            .hasSize(size);
    }

}
