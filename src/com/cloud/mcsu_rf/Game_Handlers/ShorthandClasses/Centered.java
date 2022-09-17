package com.cloud.mcsu_rf.Game_Handlers.ShorthandClasses;

import com.cloud.mcsu_rf.Definitions.Enums.CharacterSize;

import java.util.ArrayList;

public class Centered {
    private static int calcStrLen(String str) {
        int length = 0;
        for (char cha : str.toCharArray()) {
            length += CharacterSize.getCharacterWidth(cha);
        }
        return length;
    }

    public static String[] Text(String[] lines, int leftMarginPx) {

        ArrayList<String> CenteredLines = new ArrayList<>();

        for (String line : lines) {
            if ( calcStrLen(line)/2 > leftMarginPx ) {
                leftMarginPx = calcStrLen(line)/2;
            }
        }

        for (String line : lines) {
            CenteredLines.add( " ".repeat(leftMarginPx - calcStrLen(line)/2) + line );
        }

        return CenteredLines.toArray(new String[0]);

    }

}
