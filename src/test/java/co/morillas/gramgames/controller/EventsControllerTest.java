package co.morillas.gramgames.controller;

import co.morillas.gramgames.manager.EventManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class EventsControllerTest {

    @Mock
    private EventManager eventManagerMock;

    private EventsController eventsController;

    @Before
    public void setUp() {
        initMocks(this);

        eventsController = new EventsController(eventManagerMock);
    }

    @Test
    public void registerEvent_IfBodyEmptyReturnsError() {
        ResponseEntity<String> response = eventsController.registerEvent("");
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Bad request: event body null or is empty.", HttpStatus.BAD_REQUEST);

        assertThat(response, is(expectedResponse));
    }

    @Test
    public void registerEvent_IfEventWithNoClientIdReturnsError() {
        String event = "{\"event_id\" : \"...\", \"event_timestamp\" : 1, \"event_type\" : \"...\", \"event_key\" : \"...\", \"event_value\" : 1 }";

        ResponseEntity<String> response = eventsController.registerEvent(event);
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Bad request: event format invalid.", HttpStatus.BAD_REQUEST);

        assertThat(response, is(expectedResponse));
    }

    @Test
    public void registerEvent_IfEventWithNoEventIdReturnsError() throws IOException {
        String event = "{ \"client_id\" : \"...\", \"event_timestamp\" : 1, \"event_type\" : \"...\", \"event_key\" : \"...\", \"event_value\" : 1 }";

        ResponseEntity<String> response = eventsController.registerEvent(event);
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Bad request: event format invalid.", HttpStatus.BAD_REQUEST);

        assertThat(response, is(expectedResponse));
    }

    @Test
    public void registerEvent_IfEventWithNoEventKeyReturnsError() throws IOException {
        String event = "{ \"client_id\" : \"...\", \"event_id\" : \"...\", \"event_timestamp\" : 1, \"event_type\" : \"...\", \"event_value\" : 1 }";

        ResponseEntity<String> response = eventsController.registerEvent(event);
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Bad request: event format invalid.", HttpStatus.BAD_REQUEST);

        assertThat(response, is(expectedResponse));
    }

    @Test
    public void registerEvent_IfEventWithNoEventTypeReturnsError() throws IOException {
        String event = "{ \"client_id\" : \"...\", \"event_id\" : \"...\", \"event_timestamp\" : 1, \"event_key\" : \"...\", \"event_value\" : 1 }";

        ResponseEntity<String> response = eventsController.registerEvent(event);
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Bad request: event format invalid.", HttpStatus.BAD_REQUEST);

        assertThat(response, is(expectedResponse));
    }

    @Test
    public void registerEvent_IfEventCorrectReturnsOk() throws IOException {
        String event = "{ \"client_id\" : \"...\", \"event_id\" : \"...\", \"event_timestamp\" : 1, \"event_type\" : \"...\", \"event_key\" : \"...\", \"event_value\" : 1 }";

        ResponseEntity<String> response = eventsController.registerEvent(event);
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Event processed successfully", HttpStatus.OK);

        assertThat(response, is(expectedResponse));
    }

    @Test
    public void dumpEvents_IfNoClientIdInUrlReturnsError() throws Exception {
        ResponseEntity<String> response = eventsController.dumpEvents(null);
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Bad request: clientId is invalid.", HttpStatus.BAD_REQUEST);

        assertThat(response, is(expectedResponse));
    }

    @Test
    public void dumpEvents_IfWrongClientIdParameterInUrlReturnsError() throws Exception {
        ResponseEntity<String> response = eventsController.dumpEvents("");
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Bad request: clientId is invalid.", HttpStatus.BAD_REQUEST);

        assertThat(response, is(expectedResponse));
    }

    @Test
    public void dumpEvents_WorksAsExpected() throws Exception {
        String message = "this is a list of events";
        when(eventManagerMock.dumpEvents("3")).thenReturn(message);

        ResponseEntity<String> response = eventsController.dumpEvents("3");
        ResponseEntity<String> expectedResponse = new ResponseEntity<>(message, HttpStatus.OK);

        assertThat(response, is(expectedResponse));
    }
}
