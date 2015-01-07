package org.dynjs.runtime.builtins.types.regexp;

import org.jcodings.Encoding;
import org.joni.Region;

public class DynRegExpMatch {
    public final int begin;
    public final int end;
    public final String matched;

    public DynRegExpMatch(int begin, int end, String matched) {
        this.begin = begin;
        this.end = end;
        this.matched = matched;
    }

    public static DynRegExpMatch[] fromRegion(byte[] str, Encoding enc, Region region) {
        // joni returns offsets in bytes; we must convert those to characters to properly
        // deal with strings containing chars mapped to multi-byte sequences in UTF-8.
        DynRegExpMatch[] matches = new DynRegExpMatch[region.numRegs];

        for (int i = 0; i < region.numRegs; ++i) {
            if (region.beg[i] >= 0) {
                int begin = enc.strLength(str, 0, region.beg[i]);
                int end = begin + enc.strLength(str, region.beg[i], region.end[i]);
                String matched = new String(str, region.beg[i], region.end[i] - region.beg[i], enc.getCharset());
                matches[i] = new DynRegExpMatch(begin, end, matched);
            } else {
                matches[i] = new DynRegExpMatch(-1, -1, "");
            }

            /*
            String matched;
            if (region.beg[i] != -1) {
                matched = new String(str, region.beg[i], region.end[i] - region.beg[i]);
            } else {
                matched = "";
            }
            matches[i] = new DynRegExpMatch(region.beg[i], region.end[i], matched);
            */
        }

        return matches;
    }
}
