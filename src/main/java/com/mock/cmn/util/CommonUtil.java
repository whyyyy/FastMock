package com.mock.cmn.util;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class CommonUtil {

    public static String formatBizQueryParaMap(Map<String, Object> paraMap, boolean urlencode) throws Exception {

        StringBuilder buff = new StringBuilder();
        try {
            List<Entry<String, Object>> infoIds = new ArrayList<>(paraMap.entrySet());
            infoIds.sort(Comparator.comparing(o -> (o.getKey())));
            for (Entry<String, Object> item : infoIds) {
                if (!Objects.equals(item.getKey(), "")) {
                    String key = item.getKey();
                    String val = item.getValue().toString();
                    if (urlencode) {
                        val = URLEncoder.encode(val, "utf-8");
                    }
                    buff.append(key).append("=").append(val).append("&");
                }
            }

            if (buff.length() > 0) {
                buff = new StringBuilder(buff.substring(0, buff.length() - 1));
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return buff.toString();
    }
}
