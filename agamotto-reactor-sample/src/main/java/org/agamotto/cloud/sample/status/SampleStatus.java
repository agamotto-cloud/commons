package org.agamotto.cloud.sample.status;


import org.agamotto.cloud.status.BaseStatus;
import org.agamotto.cloud.status.Status;

public enum SampleStatus implements BaseStatus<SampleStatus> {

    DELETE(0, "不可用"),
    ENABLE(1, "可用"),
    ;

    private final Status status;

    SampleStatus(Integer status, String name) {
        this.status = Status.builder().status(status).name(name).build();
    }


    @Override
    public Integer value() {
        return status.getStatus();
    }
    @Override
    public Status getStatus() {
        return status;
    }

}
