package com.easysettle.service.dto;

import com.easysettle.domain.Members;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupsWithMembers {

    private Long id;

    private String name;

    private List<Members> members;

}

