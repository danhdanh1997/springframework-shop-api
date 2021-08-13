package com.xuandanh.springbootshop.pagination;

import com.xuandanh.springbootshop.domain.Actor;

import java.util.List;

public class ResponseActor {
    private List<Actor> actorList;
    private int totalPages;
    private int pageNumber;
    private int pageSize;

    public ResponseActor(){}

    public ResponseActor(List<Actor> actorList, int totalPages,
                    int pageNumber, int pageSize) {
        this.actorList  = actorList;
        this.totalPages = totalPages;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public List<Actor> getActorList() {
        return actorList;
    }

    public void setActorList(List<Actor> actorList) {
        this.actorList = actorList;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalPages() {
        return this.totalPages;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageNumber() {
        return this.pageNumber;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageSize() {
        return this.pageSize;
    }
}
