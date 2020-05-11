package com.main.david.poller;




import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class LinePoll {
    private String lineNumber;
    private Date lastPoll;
}
