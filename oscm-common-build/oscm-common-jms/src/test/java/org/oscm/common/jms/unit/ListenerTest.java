/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 23, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.jms.unit;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.TextMessage;

import org.junit.Test;
import org.mockito.Mockito;
import org.oscm.common.interfaces.data.DataType;
import org.oscm.common.interfaces.exceptions.ComponentException;
import org.oscm.common.interfaces.exceptions.ConcurrencyException;
import org.oscm.common.interfaces.exceptions.InternalException;
import org.oscm.common.interfaces.services.GenericReceiver;
import org.oscm.common.jms.Listener;
import org.oscm.common.jms.Representation;

/**
 * Unit test for Listener
 * 
 * @author miethaner
 */
public class ListenerTest extends Listener {

    private interface TestType extends DataType {
    }

    private class TestRep extends Representation implements TestType {

        @Override
        public void update() {
        }

    }

    private class TestReceiver implements GenericReceiver<TestType> {

        @Override
        public void receive(TestType content) throws ComponentException {
        }
    }

    @Test
    public void testConsumePositive() throws Exception {

        String json = "{\"id\":1, \"last_operation\":\"CREATE\"}";

        TextMessage msg = Mockito.mock(TextMessage.class);
        Mockito.when(msg.getText()).thenReturn(json);

        TestReceiver receiver = Mockito.spy(new TestReceiver());

        consumeMessage(msg, TestRep.class, receiver);

        Mockito.verify(receiver).receive(Mockito.any(TestRep.class));
    }

    @Test
    public void testConsumeNegatvieMessage() throws Exception {

        MapMessage msg = Mockito.mock(MapMessage.class);

        TestReceiver receiver = Mockito.spy(new TestReceiver());

        consumeMessage(msg, TestRep.class, receiver);

        Mockito.verify(receiver, Mockito.never()).receive(
                Mockito.any(TestRep.class));
    }

    @Test
    public void testConsumeNegativeJson() throws Exception {

        String json = "<$asdf?asdf>";

        TextMessage msg = Mockito.mock(TextMessage.class);
        Mockito.when(msg.getText()).thenReturn(json);

        TestReceiver persistence = Mockito.spy(new TestReceiver());

        consumeMessage(msg, TestRep.class, persistence);

        Mockito.verify(persistence, Mockito.never()).receive(
                Mockito.any(TestRep.class));
    }

    @Test
    public void testConsumeNegativeJms() throws Exception {

        TextMessage msg = Mockito.mock(TextMessage.class);
        Mockito.when(msg.getText()).thenThrow(
                new JMSException("because REASONS!!!"));

        TestReceiver persistence = Mockito.spy(new TestReceiver());

        consumeMessage(msg, TestRep.class, persistence);

        Mockito.verify(persistence, Mockito.never()).receive(
                Mockito.any(TestRep.class));
    }

    @Test
    public void testConsumeNegativeConcurrency() throws Exception {

        String json = "{\"id\":1, \"operation\":\"CREATE\"}";

        TextMessage msg = Mockito.mock(TextMessage.class);
        Mockito.when(msg.getText()).thenReturn(json);

        TestReceiver persistence = Mockito.spy(new TestReceiver());

        Mockito.doThrow(
                new ConcurrencyException(new Integer(1), "because REASONS!!!"))
                .when(persistence).receive(Mockito.any(TestRep.class));

        consumeMessage(msg, TestRep.class, persistence);
    }

    @Test
    public void testConsumeNegativeInternal() throws Exception {

        String json = "{\"id\":1, \"operation\":\"CREATE\"}";

        TextMessage msg = Mockito.mock(TextMessage.class);
        Mockito.when(msg.getText()).thenReturn(json);

        TestReceiver persistence = Mockito.spy(new TestReceiver());

        Mockito.doThrow(
                new InternalException(new Integer(1), "because REASONS!!!"))
                .when(persistence).receive(Mockito.any(TestRep.class));

        consumeMessage(msg, TestRep.class, persistence);
    }
}
