package com.zerobase.nsbackend.global.vo;

import java.util.Objects;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Address {
  private String streetAddress;
  private double latitude;
  private double longitude;

  private Address(String streetAddress, double latitude, double longitude) {
    this.streetAddress = streetAddress;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public static Address of(String streetAddress, double latitude, double longitude) {
    return new Address(streetAddress, latitude, longitude);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Address address = (Address) o;
    return Double.compare(address.latitude, latitude) == 0
        && Double.compare(address.longitude, longitude) == 0 && Objects.equals(
        streetAddress, address.streetAddress);
  }

  @Override
  public int hashCode() {
    return Objects.hash(streetAddress, latitude, longitude);
  }
}
