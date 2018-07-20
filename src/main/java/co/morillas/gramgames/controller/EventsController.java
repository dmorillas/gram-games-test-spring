package co.morillas.gramgames.controller;

import co.morillas.gramgames.Logger;
import co.morillas.gramgames.event.Event;
import co.morillas.gramgames.manager.EventManager;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class EventsController {

    private final EventManager eventManager;

    @Autowired
    public EventsController(final EventManager eventManager) {
        this.eventManager = eventManager;
    }

    @RequestMapping(value = "/event", method = RequestMethod.POST)
    public ResponseEntity<String> registerEvent(@RequestBody String body) {
        if(isNullOrEmpty(body)) {
            String message = "Bad request: event body null or is empty.";
            Logger.error(message);
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        Gson gson = new Gson();
        Event event = gson.fromJson(body, Event.class);
        if(!isValidEvent(event)) {
            String message = "Bad request: event format invalid.";
            Logger.error(message);
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        eventManager.addEvent(event);

        return new ResponseEntity<>("Event processed successfully", HttpStatus.OK);
    }

    private boolean isValidEvent(Event event) {
        return !isNullOrEmpty(event.getClientId()) &&
                !isNullOrEmpty(event.getId()) &&
                !isNullOrEmpty(event.getKey()) &&
                !isNullOrEmpty(event.getType());
    }

    @RequestMapping(value = "/events", method = RequestMethod.GET)
    public ResponseEntity<String> dumpEvents(@RequestParam String clientId) {
        if(isNullOrEmpty(clientId)) {
            String message = "Bad request: clientId is invalid.";
            Logger.error(message);
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        String response = eventManager.dumpEvents(clientId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }
}