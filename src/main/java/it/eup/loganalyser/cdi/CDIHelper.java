package it.eup.loganalyser.cdi;

import javax.enterprise.inject.spi.CDI;

public class CDIHelper {

	public static <T> T getBean(Class<T> requstedType) {
		return CDI.current().select(requstedType).get();
	}

	public static void fireEvent(Object event) {
		CDI.current().getBeanManager().fireEvent(event);
	}
}
