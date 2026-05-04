package net.youssfi.customerservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CustomerDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}