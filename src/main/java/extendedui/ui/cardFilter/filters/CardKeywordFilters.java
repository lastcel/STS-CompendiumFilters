package extendedui.ui.cardFilter.filters;

import basemod.abstracts.CustomCard;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.screens.compendium.CardLibSortHeader;
import extendedui.*;
import extendedui.exporter.EUIExporter;
import extendedui.interfaces.delegates.ActionT1;
import extendedui.interfaces.markers.CustomCardFilterModule;
import extendedui.interfaces.markers.CustomFilterable;
import extendedui.interfaces.markers.KeywordProvider;
import extendedui.ui.cardFilter.FilterKeywordButton;
import extendedui.ui.cardFilter.GenericFilters;
import extendedui.ui.controls.EUIDropdown;
import extendedui.ui.hitboxes.EUIHitbox;
import extendedui.ui.tooltips.EUIKeywordTooltip;
import extendedui.utilities.CostFilter;
import extendedui.utilities.EUIFontHelper;
import extendedui.utilities.FakeLibraryCard;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CardKeywordFilters extends GenericFilters<AbstractCard, CustomCardFilterModule> {
    public final HashSet<AbstractCard.CardColor> currentColors = new HashSet<>();
    public final HashSet<ModInfo> currentOrigins = new HashSet<>();
    public final HashSet<CostFilter> currentCosts = new HashSet<>();
    public final HashSet<AbstractCard.CardRarity> currentRarities = new HashSet<>();
    public final HashSet<AbstractCard.CardType> currentTypes = new HashSet<>();
    public final EUIDropdown<ModInfo> originsDropdown;
    public final EUIDropdown<CostFilter> costDropdown;
    public final EUIDropdown<AbstractCard.CardRarity> raritiesDropdown;
    public final EUIDropdown<AbstractCard.CardType> typesDropdown;
    public final EUIDropdown<AbstractCard.CardColor> colorsDropdown;

    public CardKeywordFilters() {
        super();

        originsDropdown = new EUIDropdown<ModInfo>(new EUIHitbox(0, 0, scale(240), scale(48)), c -> c == null ? EUIRM.strings.ui_basegame : c.Name)
                .setOnOpenOrClose(this::updateActive)
                .setOnChange(costs -> this.onFilterChanged(currentOrigins, costs))
                .setLabelFunctionForButton(this::filterNameFunction, false)
                .setHeader(EUIFontHelper.cardTitleFontSmall, 0.8f, Settings.GOLD_COLOR, EUIRM.strings.ui_origins)
                .setIsMultiSelect(true)
                .setCanAutosizeButton(true)
                .setItems(Loader.MODINFOS);

        costDropdown = new EUIDropdown<CostFilter>(new EUIHitbox(0, 0, scale(160), scale(48)), c -> c.name)
                .setOnOpenOrClose(this::updateActive)
                .setOnChange(costs -> this.onFilterChanged(currentCosts, costs))
                .setLabelFunctionForButton(this::filterNameFunction, false)
                .setHeader(EUIFontHelper.cardTitleFontSmall, 0.8f, Settings.GOLD_COLOR, CardLibSortHeader.TEXT[3])
                .setIsMultiSelect(true)
                .setCanAutosizeButton(true)
                .setItems(CostFilter.values());

        raritiesDropdown = new EUIDropdown<AbstractCard.CardRarity>(new EUIHitbox(0, 0, scale(240), scale(48))
                , EUIGameUtils::textForRarity)
                .setOnOpenOrClose(this::updateActive)
                .setOnChange(costs -> this.onFilterChanged(currentRarities, costs))
                .setLabelFunctionForButton(this::filterNameFunction, false)
                .setHeader(EUIFontHelper.cardTitleFontSmall, 0.8f, Settings.GOLD_COLOR, CardLibSortHeader.TEXT[0])
                .setIsMultiSelect(true)
                .setCanAutosizeButton(true)
                .setItems(AbstractCard.CardRarity.values());

        typesDropdown = new EUIDropdown<AbstractCard.CardType>(new EUIHitbox(0, 0, scale(240), scale(48))
                , EUIGameUtils::textForType)
                .setOnOpenOrClose(this::updateActive)
                .setOnChange(costs -> this.onFilterChanged(currentTypes, costs))
                .setLabelFunctionForButton(this::filterNameFunction, false)
                .setHeader(EUIFontHelper.cardTitleFontSmall, 0.8f, Settings.GOLD_COLOR, CardLibSortHeader.TEXT[1])
                .setIsMultiSelect(true)
                .setCanAutosizeButton(true)
                .setItems(AbstractCard.CardType.values());

        colorsDropdown = new EUIDropdown<AbstractCard.CardColor>(new EUIHitbox(0, 0, scale(240), scale(48))
                , EUIGameUtils::getColorName)
                .setOnOpenOrClose(this::updateActive)
                .setOnChange(costs -> this.onFilterChanged(currentColors, costs))
                .setLabelFunctionForButton(this::filterNameFunction, false)
                .setHeader(EUIFontHelper.cardTitleFontSmall, 0.8f, Settings.GOLD_COLOR, EUIRM.strings.ui_colors)
                .setIsMultiSelect(true)
                .setCanAutosizeButton(true);
    }

    public static String getDescriptionForSort(AbstractCard c) {
        if (c instanceof CustomFilterable) {
            return ((CustomFilterable) c).getDescriptionForSort();
        }
        return c.rawDescription != null ? CardModifierManager.onCreateDescription(c, c.rawDescription) : null;
    }

    public static String getNameForSort(AbstractCard c) {
        if (c instanceof CustomFilterable) {
            return ((CustomFilterable) c).getNameForSort();
        }
        return c.name != null ? CardModifierManager.onRenderTitle(c, c.name) : null;
    }

    @Override
    public int getReferenceCount() {
        return (referenceItems.size() == 1 && referenceItems.get(0) instanceof FakeLibraryCard) ? 0 : referenceItems.size();
    }

    @Override
    public void clearFilters(boolean shouldInvoke, boolean shouldClearColors) {
        if (shouldClearColors) {
            currentColors.clear();
        }
        currentOrigins.clear();
        currentFilters.clear();
        currentNegateFilters.clear();
        currentCosts.clear();
        currentRarities.clear();
        currentTypes.clear();
        currentName = null;
        currentDescription = null;
        costDropdown.setSelectionIndices((int[]) null, false);
        originsDropdown.setSelectionIndices((int[]) null, false);
        typesDropdown.setSelectionIndices((int[]) null, false);
        raritiesDropdown.setSelectionIndices((int[]) null, false);
        nameInput.setLabel("");
        descriptionInput.setLabel("");
        doForFilters(CustomCardFilterModule::reset);
    }

    public ArrayList<AbstractCard> applyFilters(ArrayList<AbstractCard> input) {
        return EUIUtils.filter(input, c -> {
            //Name check
            if (currentName != null && !currentName.isEmpty()) {
                String name = getNameForSort(c);
                if (name == null || !name.toLowerCase().contains(currentName.toLowerCase())) {
                    return false;
                }
            }

            //Description check
            if (currentDescription != null && !currentDescription.isEmpty()) {
                String desc = getDescriptionForSort(c);
                if (desc == null || !desc.toLowerCase().contains(currentDescription.toLowerCase())) {
                    return false;
                }
            }

            //Colors check
            if (!currentColors.isEmpty() && !currentColors.contains(c.color)) {
                return false;
            }

            //Origin check
            if (!evaluateItem(currentOrigins, (opt) -> EUIGameUtils.isObjectFromMod(c, opt))) {
                return false;
            }

            //Tooltips check
            if (!currentFilters.isEmpty() && (!getAllTooltips(c).containsAll(currentFilters))) {
                return false;
            }

            //Negate Tooltips check
            if (!currentNegateFilters.isEmpty() && (EUIUtils.any(getAllTooltips(c), currentNegateFilters::contains))) {
                return false;
            }

            //Rarities check
            if (!currentRarities.isEmpty() && !currentRarities.contains(c.rarity)) {
                return false;
            }

            //Types check
            if (!currentTypes.isEmpty() && !currentTypes.contains(c.type)) {
                return false;
            }

            for (CustomCardFilterModule module : EUI.globalCustomCardFilters) {
                if (!module.isItemValid(c)) {
                    return false;
                }
            }

            //Module check
            if (customModule != null && !customModule.isItemValid(c)) {
                return false;
            }

            //Cost check
            if (!currentCosts.isEmpty()) {
                boolean passes = false;
                for (CostFilter cf : currentCosts) {
                    if (cf.check(c)) {
                        passes = true;
                        break;
                    }
                }
                return passes;
            }

            return true;
        });
    }

    @Override
    public boolean areFiltersEmpty() {
        return (currentName == null || currentName.isEmpty())
                && (currentDescription == null || currentDescription.isEmpty())
                && currentColors.isEmpty() && currentOrigins.isEmpty()
                && currentFilters.isEmpty() && currentNegateFilters.isEmpty()
                && currentCosts.isEmpty() && currentRarities.isEmpty()
                && currentTypes.isEmpty()
                && EUIUtils.all(getGlobalFilters(), CustomCardFilterModule::isEmpty)
                && (customModule != null && customModule.isEmpty());
    }

    @Override
    public ArrayList<CustomCardFilterModule> getGlobalFilters() {
        return EUI.globalCustomCardFilters;
    }

    @Override
    protected void initializeImpl(ActionT1<FilterKeywordButton> onClick, ArrayList<AbstractCard> items, AbstractCard.CardColor color, boolean isAccessedFromCardPool) {
        customModule = EUI.getCustomCardFilter(color);

        HashSet<ModInfo> availableMods = new HashSet<>();
        HashSet<Integer> availableCosts = new HashSet<>();
        HashSet<AbstractCard.CardColor> availableColors = new HashSet<>();
        HashSet<AbstractCard.CardRarity> availableRarities = new HashSet<>();
        HashSet<AbstractCard.CardType> availableTypes = new HashSet<>();
        if (referenceItems != null) {
            currentTotal = getReferenceCount();
            for (AbstractCard card : referenceItems) {
                for (EUIKeywordTooltip tooltip : getAllTooltips(card)) {
                    if (tooltip.canFilter) {
                        currentFilterCounts.merge(tooltip, 1, Integer::sum);
                    }
                }

                availableMods.add(EUIGameUtils.getModInfo(card));
                availableRarities.add(card.rarity);
                availableTypes.add(card.type);
                availableCosts.add(MathUtils.clamp(card.cost, CostFilter.Unplayable.upperBound, CostFilter.Cost4Plus.lowerBound));
                availableColors.add(card.color);
            }
            doForFilters(m -> m.initializeSelection(referenceItems));
        }

        ArrayList<ModInfo> modInfos = new ArrayList<>(availableMods);
        modInfos.sort((a, b) -> a == null ? -1 : b == null ? 1 : StringUtils.compare(a.Name, b.Name));
        originsDropdown.setItems(modInfos);

        ArrayList<AbstractCard.CardRarity> rarityItems = new ArrayList<>(availableRarities);
        rarityItems.sort((a, b) -> a == null ? -1 : b == null ? 1 : a.ordinal() - b.ordinal());
        raritiesDropdown.setItems(rarityItems);

        ArrayList<AbstractCard.CardType> typesItems = new ArrayList<>(availableTypes);
        typesItems.sort((a, b) -> a == null ? -1 : b == null ? 1 : a.ordinal() - b.ordinal());
        typesDropdown.setItems(typesItems);

        ArrayList<CostFilter> costItems = new ArrayList<>();
        for (CostFilter c : CostFilter.values()) {
            if (availableCosts.contains(c.lowerBound) || availableCosts.contains(c.upperBound)) {
                costItems.add(c);
            }
        }
        costDropdown.setItems(costItems);

        ArrayList<AbstractCard.CardColor> colorsItems = new ArrayList<>(availableColors);
        colorsItems.sort((a, b) -> a == AbstractCard.CardColor.COLORLESS ? -1 : a == AbstractCard.CardColor.CURSE ? -2 : StringUtils.compare(a.name(), b.name()));
        colorsDropdown.setItems(colorsItems);
        if (isAccessedFromCardPool) {
            ArrayList<AbstractCard.CardColor> filteredColors = EUIUtils.filter(colorsItems, c -> c != AbstractCard.CardColor.COLORLESS && c != AbstractCard.CardColor.CURSE);
            colorsDropdown.setSelection(filteredColors, true).setActive(false);
        }
        else {
            colorsDropdown.setActive(colorsItems.size() > 1);
        }
    }

    public void toggleFilters() {
        if (isActive) {
            close();
        }
        else {
            open();
        }
    }

    @Override
    public void updateFilters() {
        originsDropdown.setPosition(hb.x - SPACING * 3.65f, DRAW_START_Y + scrollDelta).tryUpdate();
        if (colorsDropdown.isActive) {
            colorsDropdown.setPosition(originsDropdown.hb.x + originsDropdown.hb.width + SPACING * 2, DRAW_START_Y + scrollDelta).tryUpdate();
            costDropdown.setPosition(colorsDropdown.hb.x + colorsDropdown.hb.width + SPACING * 2, DRAW_START_Y + scrollDelta).tryUpdate();
        }
        else {
            costDropdown.setPosition(originsDropdown.hb.x + originsDropdown.hb.width + SPACING * 2, DRAW_START_Y + scrollDelta).tryUpdate();
        }
        raritiesDropdown.setPosition(costDropdown.hb.x + costDropdown.hb.width + SPACING * 2, DRAW_START_Y + scrollDelta).tryUpdate();
        typesDropdown.setPosition(raritiesDropdown.hb.x + raritiesDropdown.hb.width + SPACING * 2, DRAW_START_Y + scrollDelta).tryUpdate();
        nameInput.setPosition(hb.x + SPACING * 5.15f, DRAW_START_Y + scrollDelta - SPACING * 3.8f).tryUpdate();
        descriptionInput.setPosition(nameInput.hb.cX + nameInput.hb.width + SPACING * 2.95f, DRAW_START_Y + scrollDelta - SPACING * 3.8f).tryUpdate();
        doForFilters(CustomCardFilterModule::update);
    }

    public List<EUIKeywordTooltip> getAllTooltips(AbstractCard c) {
        KeywordProvider eC = EUIUtils.safeCast(c, KeywordProvider.class);
        if (eC != null) {
            return eC.getTipsForFilters();
        }

        ArrayList<EUIKeywordTooltip> dynamicTooltips = new ArrayList<>();
        if (c.isInnate) {
            EUIKeywordTooltip tip = EUIKeywordTooltip.findByID(GameDictionary.EXHAUST.NAMES[0]);
            if (tip != null) {
                dynamicTooltips.add(tip);
            }
        }
        if (c.isEthereal) {
            EUIKeywordTooltip tip = EUIKeywordTooltip.findByID(GameDictionary.ETHEREAL.NAMES[0]);
            if (tip != null) {
                dynamicTooltips.add(tip);
            }
        }
        if (c.selfRetain) {
            EUIKeywordTooltip tip = EUIKeywordTooltip.findByID(GameDictionary.RETAIN.NAMES[0]);
            if (tip != null) {
                dynamicTooltips.add(tip);
            }
        }
        if (c.exhaust) {
            EUIKeywordTooltip tip = EUIKeywordTooltip.findByID(GameDictionary.EXHAUST.NAMES[0]);
            if (tip != null) {
                dynamicTooltips.add(tip);
            }
        }
        for (String sk : c.keywords) {
            EUIKeywordTooltip tip = EUIKeywordTooltip.findByName(sk);
            if (tip != null && !dynamicTooltips.contains(tip)) {
                dynamicTooltips.add(tip);
            }
        }
        if (c instanceof CustomCard) {
            List<TooltipInfo> infos = ((CustomCard) c).getCustomTooltips();
            ModInfo mi = EUIGameUtils.getModInfo(c);
            if (infos != null && mi != null) {
                for (TooltipInfo info : infos) {
                    EUIKeywordTooltip tip = EUIKeywordTooltip.findByName(mi.ID.toLowerCase() + ":" + info.title);
                    if (tip != null && !dynamicTooltips.contains(tip)) {
                        dynamicTooltips.add(tip);
                    }
                }
            }
        }
        return dynamicTooltips;
    }

    @Override
    public boolean isHoveredImpl() {
        return originsDropdown.areAnyItemsHovered()
                || colorsDropdown.areAnyItemsHovered()
                || costDropdown.areAnyItemsHovered()
                || raritiesDropdown.areAnyItemsHovered()
                || typesDropdown.areAnyItemsHovered()
                || nameInput.hb.hovered
                || descriptionInput.hb.hovered
                || EUIUtils.any(getGlobalFilters(), CustomCardFilterModule::isHovered)
                || (customModule != null && customModule.isHovered());
    }

    @Override
    public void renderFilters(SpriteBatch sb) {
        originsDropdown.tryRender(sb);
        colorsDropdown.tryRender(sb);
        costDropdown.tryRender(sb);
        raritiesDropdown.tryRender(sb);
        typesDropdown.tryRender(sb);
        nameInput.tryRender(sb);
        descriptionInput.tryRender(sb);

        for (CustomCardFilterModule module : EUI.globalCustomCardFilters) {
            module.render(sb);
        }
        if (customModule != null) {
            customModule.render(sb);
        }
        doForFilters(m -> m.render(sb));
    }

    public CardKeywordFilters initializeForCustomHeader(CardGroup group, ActionT1<FilterKeywordButton> onClick, AbstractCard.CardColor color, boolean isAccessedFromCardPool, boolean isFixedPosition) {
        EUI.customHeader.setGroup(group);
        EUI.customHeader.setupButtons(isFixedPosition);
        initialize((button) ->
        {
            EUI.customHeader.updateForFilters();
            onClick.invoke(button);
        }, EUI.customHeader.group.group, color, isAccessedFromCardPool);
        EUI.customHeader.updateForFilters();
        EUIExporter.exportCardButton.setOnClick(() -> EUIExporter.openForCards(EUI.customHeader.group.group));
        return this;
    }

    public CardKeywordFilters initializeForCustomHeader(CardGroup group, AbstractCard.CardColor color, boolean isAccessedFromCardPool, boolean isFixedPosition) {
        EUI.customHeader.setGroup(group);
        EUI.customHeader.setupButtons(isFixedPosition);
        initialize((button) ->
        {
            EUI.customHeader.updateForFilters();
        }, EUI.customHeader.group.group, color, isAccessedFromCardPool);
        EUI.customHeader.updateForFilters();
        EUIExporter.exportCardButton.setOnClick(() -> EUIExporter.openForCards(EUI.customHeader.group.group));
        return this;
    }
}