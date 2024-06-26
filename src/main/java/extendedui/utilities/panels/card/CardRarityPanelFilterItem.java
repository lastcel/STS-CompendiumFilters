package extendedui.utilities.panels.card;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import extendedui.EUIGameUtils;
import extendedui.EUIRM;
import extendedui.interfaces.markers.CountingPanelItem;
import extendedui.ui.tooltips.EUITooltip;

public class CardRarityPanelFilterItem implements CountingPanelItem<AbstractCard> {
    protected static final CardRarityPanelFilterItem BASIC = new CardRarityPanelFilterItem(AbstractCard.CardRarity.BASIC);
    protected static final CardRarityPanelFilterItem COMMON = new CardRarityPanelFilterItem(AbstractCard.CardRarity.COMMON);
    protected static final CardRarityPanelFilterItem UNCOMMON = new CardRarityPanelFilterItem(AbstractCard.CardRarity.UNCOMMON);
    protected static final CardRarityPanelFilterItem RARE = new CardRarityPanelFilterItem(AbstractCard.CardRarity.RARE);
    protected static final CardRarityPanelFilterItem CURSE = new CardRarityPanelFilterItem(AbstractCard.CardRarity.CURSE);
    protected static final CardRarityPanelFilterItem SPECIAL = new CardRarityPanelFilterItem(AbstractCard.CardRarity.SPECIAL);
    public final AbstractCard.CardRarity rarity;

    public CardRarityPanelFilterItem(AbstractCard.CardRarity rarity) {
        this.rarity = rarity;
    }

    public static CardRarityPanelFilterItem get(AbstractCard.CardRarity rarity) {
        switch (rarity) {
            case BASIC:
                return BASIC;
            case COMMON:
                return COMMON;
            case RARE:
                return RARE;
            case UNCOMMON:
                return UNCOMMON;
            case CURSE:
                return CURSE;
            default:
                return SPECIAL;
        }
    }

    @Override
    public Color getColor() {
        return EUIGameUtils.colorForRarity(rarity);
    }

    @Override
    public Texture getIcon() {
        return EUIRM.images.squaredButton2.texture();
    }

    @Override
    public EUITooltip getTipForButton() {
        return new EUITooltip(EUIGameUtils.textForRarity(rarity), EUIRM.strings.misc_countPanelItem);
    }

    @Override
    public int getRank(AbstractCard c) {
        int ordinal = c.rarity.ordinal();
        return c.rarity == rarity ? ordinal + 1000 : ordinal;
    }
}
