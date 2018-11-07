package com.example.oliverh.bakerapp.data.network;

import java.util.ArrayList;
import java.util.List;

public class RepositoryResponse<T> {
    private List<T> listOfData;
    private Throwable error;

    public RepositoryResponse(List<T> data) {
        this.listOfData = data;
        this.error = null;
    }

    public RepositoryResponse(Throwable err) {
        this.listOfData = new ArrayList<>();
        this.error = err;
    }

    public List<T> getListOfData() {
        return listOfData;
    }

    public void setListOfData(List<T> listOfData) {
        this.listOfData = listOfData;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }
}
