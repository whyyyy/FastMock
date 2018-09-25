package com.mock.http;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpResponse {
	private String content = "";
	private String version = "";
	private int status = 0;
	private StringBuilder reason = new StringBuilder();
	private Map<String, String> headers = new HashMap<>();

	public void setHeader(Map<String, List<String>> h) {
		Set<Entry<String, List<String>>> set = h.entrySet();
		for (Entry<String, List<String>> entry : set) {
			List<String> values = entry.getValue();

			if (entry.getKey() == null) {
				String v[] = values.get(0).split(" ");
				version = v[0];
				status = Integer.valueOf(v[1]);
				for (int inx = 2; inx < v.length; inx++) {
					reason.append(v[inx]).append(" ");
				}
			} else {
				headers.put(entry.getKey(), values.get(0));
			}
		}
	}

	public String Content() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int Status() {
		return this.status;
	}

	public StringBuilder Reason() {
		return this.reason;
	}

	public void showStatuReason() {
		log.debug("{} {} {} ", this.status, this.reason, this.version);

	}

	public String getHeader(String name) {
		return headers.get(name);

	}

    public Map<String, String> getHeaders() {
        return headers;

    }

}
