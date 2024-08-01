package com.appli.clcapi.common.response;

public class PaginatedResponse extends NonPaginatedResponse{
    private int pageSize;
    private int currentPage;
    private int totalPages;

    public PaginatedResponse(){
        setPaginated(Boolean.TRUE);
    }
}
