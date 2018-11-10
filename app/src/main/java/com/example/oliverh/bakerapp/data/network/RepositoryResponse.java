package com.example.oliverh.bakerapp.data.network;

import java.util.ArrayList;
import java.util.List;

public class RepositoryResponse<T> {
    private List<T> listOfData;
    private T obj;
    private Throwable error;

    public RepositoryResponse(List<T> data) {
        this.listOfData = data;
        this.obj = null;
        this.error = null;
    }

    public RepositoryResponse(T data) {
        this.obj = data;
        this.listOfData = null;
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

    public T getObject() { return obj; }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }
}
