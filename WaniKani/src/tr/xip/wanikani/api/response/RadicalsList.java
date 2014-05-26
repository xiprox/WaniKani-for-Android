package tr.xip.wanikani.api.response;

import java.util.List;

/**
 * Created by xihsa_000 on 3/12/14.
 */
public class RadicalsList {
    private UserInfo user_information;
    private List<RadicalItem> requested_information;
    private Error error;

    public UserInfo getUserInfo() {
        return user_information;
    }

    public List<RadicalItem> getList() {
        return requested_information;
    }

    public Error getError() {
        return error;
    }

    public class RadicalItem {
        private String character;
        private String meaning;
        private String image;
        private int level;
        private UserSpecific user_specific;

        private class UserSpecific {
            private String srs;
            private long unlocked_date;
            private long available_date;
            private boolean burned;
            private long burned_date;
            private int meaning_correct;
            private int meaning_incorrect;
            private int meaning_max_streak;
            private int meaning_current_streak;
            private String meaning_note;
            private String[] user_synonyms;
        }

        public String getCharacter() {
            return character;
        }

        public String getMeaning() {
            return meaning;
        }

        public String getImage() {
            return image;
        }

        public int getLevel() {
            return level;
        }

        public boolean isUnlocked() {
            return user_specific != null;
        }

        public String getSrsLevel() {
            return user_specific.srs;
        }

        public long getUnlockDate() {
            return user_specific.unlocked_date * 1000;
        }

        public long getAvailableDate() {
            return user_specific.available_date * 1000;
        }

        public boolean isBurned() {
            return user_specific.burned;
        }

        public long getBurnedDate() {
            return user_specific.burned_date * 1000;
        }

        public int getMeaningCorrect() {
            return user_specific.meaning_correct;
        }

        public int getMeaningIncorrect() {
            return user_specific.meaning_incorrect;
        }

        public int getMeaningMaxStreak() {
            return user_specific.meaning_max_streak;
        }

        public int getMeaningCurrentStreak() {
            return user_specific.meaning_current_streak;
        }

        public int getMeaningAnswersCount() {
            return user_specific.meaning_correct + user_specific.meaning_incorrect;
        }

        public int getMeaningCorrectPercentage() {
            return (int) ((double) user_specific.meaning_correct / getMeaningAnswersCount() * 100);
        }

        public int getMeaningIncorrectPercentage() {
            return (int) ((double) user_specific.meaning_incorrect / getMeaningAnswersCount() * 100);
        }

        public String getMeaningNote() {
            return user_specific.meaning_note;
        }

        public String[] getUserSynonyms() {
            return user_specific.user_synonyms;
        }
    }
}
