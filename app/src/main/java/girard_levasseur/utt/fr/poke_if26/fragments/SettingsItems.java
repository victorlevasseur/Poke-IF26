package girard_levasseur.utt.fr.poke_if26.fragments;

/**
 * Enum of the settings items with their index displayed in the account settings fragment.
 */
enum SettingsItems {
    CHANGE_PASSWORD_ITEM(0),
    CHANGE_LOGIN_ITEM(1),
    DELETE_ACCOUNT(2);

    private int index;

    SettingsItems(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
