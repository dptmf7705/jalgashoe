package com.dankook.jalgashoe.util;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Created by yeseul on 2018-05-22.
 */

public class XmlParserUtil {

    public static String getTagValue(String tag, Element element) {
        Node node = element.getElementsByTagName(tag).item(0);
        if(node == null) {
            return null;
        }
        return node.getTextContent();
    }

    public static int getIntTagValue(String tag, Element element) {
        String value = getTagValue(tag, element);
        if (value == null){
            return 0;
        }
        String result = value.replace(" ", "");
        if(result == null || result.equals("")){
            return 0;
        }
        return Integer.parseInt(result);
    }

    public static double getDoubleTagValue(String tag, Element element) {
        String value = getTagValue(tag, element);
        if (value == null){
            return 0;
        }
        String result = value.replace(" ", "");
        if(result == null || result.equals("")){
            return 0;
        }
        return Double.parseDouble(result);
    }

}
