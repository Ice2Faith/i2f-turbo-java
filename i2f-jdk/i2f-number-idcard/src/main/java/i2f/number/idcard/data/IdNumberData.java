package i2f.number.idcard.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
public class IdNumberData {
    public static SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");
    public String idNumber;
    public String region;
    public String regionDesc;
    public String date;
    public Date dateDesc;
    public String year;
    public String month;
    public String day;
    public boolean isLeap = false;
    public String policy;
    public String sex;
    public String sexDesc;
    public String checkSum;
    public boolean isLegalCheckSum = false;
    public boolean isLegalId = false;
    public String illegalReason;
}
