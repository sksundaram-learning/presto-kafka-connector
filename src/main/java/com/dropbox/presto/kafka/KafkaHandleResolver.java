/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dropbox.presto.kafka;

import static com.google.common.base.Preconditions.checkNotNull;

import com.facebook.presto.hive.HiveColumnHandle;
import com.facebook.presto.spi.ColumnHandle;
import com.facebook.presto.spi.ConnectorHandleResolver;
import com.facebook.presto.spi.Split;
import com.facebook.presto.spi.TableHandle;
import com.google.inject.Inject;

public class KafkaHandleResolver implements ConnectorHandleResolver
{

    private final String connectorId;

    @Inject
    public KafkaHandleResolver(KafkaConnectorId id)
    {
        this.connectorId = checkNotNull(id).toString();
    }

    @Override
    public boolean canHandle(TableHandle tableHandle)
    {
        return tableHandle instanceof KafkaTableHandle
                && ((KafkaTableHandle) tableHandle).isKafkaTable(connectorId);
    }

    @Override
    public boolean canHandle(ColumnHandle columnHandle)
    {
      return columnHandle instanceof HiveColumnHandle
          && ((HiveColumnHandle) columnHandle).getClientId().equals(connectorId);
    }

    @Override
    public boolean canHandle(Split split)
    {
        return split instanceof KafkaSplit
                && ((KafkaSplit) split).getClientId().equals(connectorId);
    }

    @Override
    public Class<? extends TableHandle> getTableHandleClass()
    {
        return KafkaTableHandle.class;
    }

    @Override
    public Class<? extends ColumnHandle> getColumnHandleClass()
    {
        return HiveColumnHandle.class;
    }

    @Override
    public Class<? extends Split> getSplitClass()
    {
        return KafkaSplit.class;
    }

}