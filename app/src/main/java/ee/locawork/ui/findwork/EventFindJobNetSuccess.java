package ee.locawork.ui.findwork;

import ee.locawork.model.Job;

import java.util.List;

import retrofit2.Response;

public class EventFindJobNetSuccess {
    private Response<List<Job>> jobDTO;

    public EventFindJobNetSuccess(Response<List<Job>> response) {
        this.jobDTO = response;
    }

    public Response<List<Job>> getJobDTO() {
        return this.jobDTO;
    }

    public void setJobDTO(Response<List<Job>> jobDTO) {
        this.jobDTO = jobDTO;
    }
}
