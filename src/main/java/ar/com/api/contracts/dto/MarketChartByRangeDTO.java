package ar.com.api.contracts.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MarketChartByRangeDTO implements IFilterDTO {

 private String id;
 private String contractAddress;
 private String vsCurrency;
 private String fromDate;
 private String toDate;

 @Override
 public String getUrlFilterService() {

  StringBuilder urlServicBuilder = new StringBuilder();
  urlServicBuilder.append("/?vs_currency=").append(vsCurrency)
     .append("&from=").append(fromDate)
     .append("&to=").append(toDate);
  
  return urlServicBuilder.toString();
 }
 
}
