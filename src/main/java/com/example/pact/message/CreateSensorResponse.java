package com.example.pact.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
public class CreateSensorResponse {
    private List<String> sensors;
    private boolean isSuccess;


    public List<String> getSensors() {
        return sensors;
    }

    public boolean getIsSuccess(){
        return isSuccess;
    }

    public void setSensors(List<String> sensors) {
        this.sensors = sensors;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}
