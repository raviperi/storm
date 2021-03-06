/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.apache.storm.eventhubs.trident;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.apache.storm.eventhubs.core.EventHubMessage;
import org.apache.storm.eventhubs.core.FieldConstants;
import org.apache.storm.eventhubs.core.IEventHubReceiver;
import org.apache.storm.eventhubs.core.OffsetFilter;
import org.apache.storm.eventhubs.core.TimestampFilter;
import org.apache.storm.eventhubs.spout.EventHubSpoutConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.servicebus.ServiceBusException;

public class TridentPartitionManager implements ITridentPartitionManager {
	private static final Logger logger = LoggerFactory.getLogger(TridentPartitionManager.class);
	private final IEventHubReceiver receiver;
	private final EventHubSpoutConfig spoutConfig;
	private String lastOffset = FieldConstants.DefaultStartingOffset;
	private String partitionId;

	public TridentPartitionManager(EventHubSpoutConfig spoutConfig, IEventHubReceiver receiver, String partitionId) {
		this.receiver = receiver;
		this.spoutConfig = spoutConfig;
		this.partitionId = partitionId;
	}

	@Override
	public void open(String offset) throws IOException, ServiceBusException {
		logger.debug("Creating EventHub Client");
		if ((offset == null || offset.equals(FieldConstants.DefaultStartingOffset))
				&& spoutConfig.getEnqueueTimeFilter() != 0) {
			receiver.open(new TimestampFilter(Instant.ofEpochMilli(spoutConfig.getEnqueueTimeFilter())));
		} else {
			receiver.open(new OffsetFilter(offset));
		}
		lastOffset = offset;
	}

	@Override
	public void close() {
		receiver.close();
	}

	@Override
	public List<EventHubMessage> receiveBatch(String offset, int count) throws IOException, ServiceBusException {
		List<EventHubMessage> batch = new ArrayList<EventHubMessage>(spoutConfig.getReceiveEventsMaxCount());
		if (!offset.equals(lastOffset) || !receiver.isOpen()) {
			close();
			open(offset);
		}

		Iterable<EventData> messages = receiver.receive(count);

		for (EventData ed : messages) {
			EventHubMessage ehm = new EventHubMessage(ed, partitionId);
			batch.add(ehm);
			lastOffset = ehm.getOffset();
		}

		return batch;
	}

	@Override
	public String getPartitionId() {
		return partitionId;
	}
}
