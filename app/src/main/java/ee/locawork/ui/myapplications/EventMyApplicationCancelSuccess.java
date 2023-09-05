package ee.locawork.ui.myapplications;

import ee.locawork.model.MyApplicationDTO;

public class EventMyApplicationCancelSuccess {
    private MyApplicationDTO myApplicationDTO;

    public EventMyApplicationCancelSuccess(MyApplicationDTO myApplicationDTO) {
        this.myApplicationDTO = myApplicationDTO;
    }

    public MyApplicationDTO getMyApplicationDTO() {
        return this.myApplicationDTO;
    }

    public void setMyApplicationDTO(MyApplicationDTO jobApplications2) {
        this.myApplicationDTO = jobApplications2;
    }
}
