package com.zerobase.nsbackend.errand.dto;

import com.zerobase.nsbackend.global.vo.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class ErrandChangAddressRequest {
  private String streetAddress;
  private Double latitude;
  private Double longitude;

  public Address toAddress() {
    return Address.of(streetAddress, latitude, longitude);
  }
}
