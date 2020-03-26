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
package com.facebook.presto.druid.column;

import com.facebook.presto.spi.block.Block;
import com.facebook.presto.spi.block.BlockBuilder;
import com.facebook.presto.spi.type.Type;
import io.airlift.slice.Slices;
import org.apache.druid.segment.ColumnValueSelector;

import static com.facebook.presto.spi.type.VarcharType.VARCHAR;
import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

public class StringColumnReader
        implements ColumnReader
{
    private final ColumnValueSelector<String> valueSelector;

    public StringColumnReader(ColumnValueSelector valueSelector)
    {
        this.valueSelector = requireNonNull(valueSelector, "value selector is null");
    }

    @Override
    public Block readBlock(Type type, int batchSize)
    {
        checkArgument(type == VARCHAR);
        BlockBuilder builder = type.createBlockBuilder(null, batchSize);
        for (int i = 0; i < batchSize; i++) {
            String value = valueSelector.getObject();
            if (value != null) {
                type.writeSlice(builder, Slices.utf8Slice(value));
            }
            else {
                builder.appendNull();
            }
        }

        return builder.build();
    }
}