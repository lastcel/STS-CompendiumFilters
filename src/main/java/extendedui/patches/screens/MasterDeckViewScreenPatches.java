package extendedui.patches.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.MasterDeckViewScreen;
import extendedui.EUI;
import extendedui.ui.screens.CardPoolScreen;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;

import java.util.ArrayList;

import static extendedui.ui.cardFilter.CustomCardLibSortHeader.getFakeGroup;

public class MasterDeckViewScreenPatches {
    public static final CardGroup fakeMasterDeck = new CardGroup(CardGroup.CardGroupType.MASTER_DECK);
    public static MasterDeckViewScreen screen; // Can be used for card pool modules to reference

    public static void updateForFilters() {
        if (EUI.cardFilters.areFiltersEmpty()) {
            getFakeMasterDeck();
        }
        else {
            ArrayList<AbstractCard> tempGroup = EUI.cardFilters.applyFilters(AbstractDungeon.player.masterDeck.group);
            if (tempGroup.size() > 0) {
                fakeMasterDeck.group = tempGroup;
            }
            else if (!EUI.cardFilters.areFiltersEmpty()) {
                EUI.cardFilters.currentFilters.clear();
                tempGroup = EUI.cardFilters.applyFilters(AbstractDungeon.player.masterDeck.group);
                fakeMasterDeck.group = tempGroup.size() > 0 ? tempGroup : getFakeGroup();
            }
            else {
                fakeMasterDeck.group = getFakeGroup();
            }
        }
        EUI.cardFilters.refresh(fakeMasterDeck.group);
    }

    public static CardGroup getFakeMasterDeck() {
        fakeMasterDeck.clear();
        if (AbstractDungeon.player != null) {
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                fakeMasterDeck.addToBottom(c);
            }
        }
        return fakeMasterDeck;
    }

    @SpirePatch(clz = MasterDeckViewScreen.class, method = "open")
    public static class MasterDeckViewScreen_Open {
        @SpirePrefixPatch
        public static void prefix(MasterDeckViewScreen __instance) {
            screen = __instance;
            getFakeMasterDeck();
            EUI.cardFilters.initialize(__ -> {
                updateForFilters();
                if (CardPoolScreen.customModule != null) {
                    CardPoolScreen.customModule.open(fakeMasterDeck.group);
                }
            }, fakeMasterDeck.group, AbstractDungeon.player != null ? AbstractDungeon.player.getCardColor() : AbstractCard.CardColor.COLORLESS, false);
            updateForFilters();
            EUI.countingPanel.open(AbstractDungeon.player.masterDeck.group);
        }
    }

    @SpirePatch(clz = MasterDeckViewScreen.class, method = "update")
    public static class MasterDeckViewScreen_Update {
        @SpirePrefixPatch
        public static void prefix(MasterDeckViewScreen __instance) {
            if (!EUI.cardFilters.tryUpdate() && EUI.openCardFiltersButton != null) {
                EUI.openCardFiltersButton.tryUpdate();
                EUI.countingPanel.tryUpdate();
            }
        }
    }

    @SpirePatch(clz = MasterDeckViewScreen.class, method = "render", paramtypez = {SpriteBatch.class})
    public static class MasterDeckViewScreen_Render {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return new ExprEditor() {
                public void edit(javassist.expr.FieldAccess m) throws CannotCompileException {
                    if (m.getClassName().equals(AbstractPlayer.class.getName()) && m.getFieldName().equals("masterDeck")) {
                        m.replace("{ $_ = extendedui.patches.screens.MasterDeckViewScreenPatches.fakeMasterDeck; }");
                    }
                }
            };
        }

        @SpirePrefixPatch
        public static void prefix(MasterDeckViewScreen __instance, SpriteBatch sb) {
            if (!EUI.cardFilters.isActive) {
                EUI.openCardFiltersButton.tryRender(sb);
                EUI.countingPanel.tryRender(sb);
            }
        }
    }

    @SpirePatch(clz = MasterDeckViewScreen.class, method = "hideCards")
    public static class MasterDeckViewScreen_HideCards {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return new ExprEditor() {
                public void edit(javassist.expr.FieldAccess m) throws CannotCompileException {
                    if (m.getClassName().equals(CardGroup.class.getName()) && m.getFieldName().equals("group")) {
                        m.replace("{ $_ = extendedui.patches.screens.MasterDeckViewScreenPatches.fakeMasterDeck.group; }");
                    }
                }
            };
        }
    }

    @SpirePatch(clz = MasterDeckViewScreen.class, method = "updatePositions")
    public static class MasterDeckViewScreen_UpdatePositions {

        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return new ExprEditor() {
                public void edit(javassist.expr.FieldAccess m) throws CannotCompileException {
                    if (m.getClassName().equals(CardGroup.class.getName()) && m.getFieldName().equals("group")) {
                        m.replace("{ $_ = extendedui.patches.screens.MasterDeckViewScreenPatches.fakeMasterDeck.group; }");
                    }
                }
            };
        }
    }

    @SpirePatch(clz = MasterDeckViewScreen.class, method = "updateControllerInput")
    public static class MasterDeckViewScreen_UpdateControllerInput {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return new ExprEditor() {
                public void edit(javassist.expr.FieldAccess m) throws CannotCompileException {
                    if (m.getClassName().equals(AbstractPlayer.class.getName()) && m.getFieldName().equals("masterDeck")) {
                        m.replace("{ $_ = extendedui.patches.screens.MasterDeckViewScreenPatches.fakeMasterDeck; }");
                    }
                }
            };
        }
    }

    @SpirePatch(clz = MasterDeckViewScreen.class, method = "updateClicking")
    public static class MasterDeckViewScreen_UpdateClicking {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return new ExprEditor() {
                public void edit(javassist.expr.FieldAccess m) throws CannotCompileException {
                    if (m.getClassName().equals(AbstractPlayer.class.getName()) && m.getFieldName().equals("masterDeck")) {
                        m.replace("{ $_ = extendedui.patches.screens.MasterDeckViewScreenPatches.fakeMasterDeck; }");
                    }
                }
            };
        }
    }
}
