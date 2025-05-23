/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.gluten.component

import org.apache.gluten.backendsapi.clickhouse.CHBackend
import org.apache.gluten.execution.OffloadKafkaScan
import org.apache.gluten.extension.columnar.KafkaMiscColumnarRules.RemoveStreamingTopmostColumnarToRow
import org.apache.gluten.extension.injector.Injector

class CHKafkaComponent extends Component {
  override def name(): String = "clickhouse-kafka"
  override def buildInfo(): Component.BuildInfo =
    Component.BuildInfo("ClickHouseKafka", "N/A", "N/A", "N/A")
  override def dependencies(): Seq[Class[_ <: Component]] = classOf[CHBackend] :: Nil
  override def injectRules(injector: Injector): Unit = {
    OffloadKafkaScan.inject(injector)
    injector.gluten.legacy.injectPost(c => RemoveStreamingTopmostColumnarToRow(c.session, c.caller.isStreaming()))
  }
}
