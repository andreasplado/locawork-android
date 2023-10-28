package ee.locawork;

import ee.locawork.model.User;
import retrofit2.Response;

public class EventGetEmployerData {
    private Response<User> response;
    public EventGetEmployerData(Response<User> response) {
        this.response = response;
    }

    public Response<User> getResponse() {
        return response;
    }
}
