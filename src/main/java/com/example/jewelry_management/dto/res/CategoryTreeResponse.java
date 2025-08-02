package com.example.jewelry_management.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryTreeResponse {
    private Integer id;
    private String name;
    private String bannerUrl;
    private List<CategoryTreeResponse> children;
}