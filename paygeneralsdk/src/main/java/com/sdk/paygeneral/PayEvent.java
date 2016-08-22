package com.sdk.paygeneral;

import com.thinkcore.event.TEvent;
import com.thinkcore.event.TEventIdUtils;

public class PayEvent extends TEvent {

	public static final int Event_Pay_Result = TEventIdUtils.getNextEvent();
	public static final int Event_Pay_Success = TEventIdUtils.getNextEvent();
	public static final int Event_Pay_Fail = TEventIdUtils.getNextEvent();
	public static final int Event_Alipay_Check = TEventIdUtils.getNextEvent();

	public PayEvent(int mainEvent) {
		super(mainEvent);
	}

	public PayEvent(int mainEvent, int subEvent) {
		super(mainEvent, subEvent);
	}

	public PayEvent(int mainEvent, int subEvent, Object params) {
		super(mainEvent, subEvent, params);
	}

	public PayEvent(int mainEvent, Object params) {
		super(mainEvent, params);
	}
}
