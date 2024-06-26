package extendedui.utilities.panels.card;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.screens.compendium.CardLibSortHeader;
import extendedui.interfaces.delegates.ActionT1;
import extendedui.interfaces.markers.CountingPanelFilter;
import extendedui.interfaces.markers.CountingPanelItem;
import extendedui.ui.cardFilter.CountingPanelCounter;
import extendedui.ui.cardFilter.CountingPanelStats;

import java.util.ArrayList;
import java.util.Collections;

public class CardTypePanelFilter implements CountingPanelFilter<AbstractCard> {
    @Override
    public ArrayList<? extends CountingPanelCounter<?, AbstractCard>> generateCounters(ArrayList<? extends AbstractCard> cards, Hitbox hb, ActionT1<CountingPanelCounter<? extends CountingPanelItem<AbstractCard>, AbstractCard>> onClick) {
        return CountingPanelStats.basic(c ->
                        Collections.singleton(CardTypePanelFilterItem.get(c.type)),
                cards).generateCounters(hb, onClick);
    }

    @Override
    public String getTitle() {
        return CardLibSortHeader.TEXT[1];
    }
}
