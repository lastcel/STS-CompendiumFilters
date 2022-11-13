package extendedui.ui.controls;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import extendedui.utilities.Mathf;

public class EUIStaticRelicGrid extends EUIRelicGrid
{
    protected int currentRow;
    public int visibleRowCount = 20;

    public EUIStaticRelicGrid()
    {
        this(0.5f, true);
    }

    public EUIStaticRelicGrid(float horizontalAlignment)
    {
        this(horizontalAlignment, true);
    }

    public EUIStaticRelicGrid(float horizontalAlignment, boolean autoShowScrollbar)
    {
        super(horizontalAlignment, autoShowScrollbar);
        instantSnap = true;
    }

    @Override
    protected void UpdateRelics()
    {
        hoveredRelic = null;

        int row = 0;
        int column = 0;

        for (int i = Math.max(0, currentRow * rowSize); i < Math.min((currentRow + visibleRowCount) * rowSize, relicGroup.size()); i++) {
            RelicInfo relic = relicGroup.get(i);
            relic.relic.currentX = relic.relic.targetX = (DRAW_START_X * draw_x) + (column * PAD);
            relic.relic.currentY = relic.relic.targetY = draw_top_y - (row * pad_y);
            UpdateHoverLogic(relic, i);

            column += 1;
            if (column >= rowSize)
            {
                column = 0;
                row += 1;
            }
        }
    }

    public void ForceUpdateRelicPositions()
    {
        int row = 0;
        int column = 0;
        for (int i = Math.max(0, currentRow * rowSize); i < Math.min((currentRow + visibleRowCount) * rowSize, relicGroup.size()); i++)
        {
            RelicInfo relic = relicGroup.get(i);
            relic.relic.currentX = relic.relic.targetX = (DRAW_START_X * draw_x) + (column * PAD);
            relic.relic.currentY = relic.relic.targetY = draw_top_y + scrollDelta - (row * pad_y);
            relic.relic.hb.update();
            relic.relic.hb.move(relic.relic.currentX, relic.relic.currentY);

            column += 1;
            if (column >= rowSize)
            {
                column = 0;
                row += 1;
            }
        }
    }

    @Override
    protected void RenderRelics(SpriteBatch sb)
    {
        for (int i = Math.max(0, currentRow * rowSize); i < Math.min((currentRow + visibleRowCount) * rowSize, relicGroup.size()); i++)
        {
            RenderRelic(sb, relicGroup.get(i));
        }
    }

    // TODO Remove, probably not necessary
    @Override
    protected void UpdateScrolling(boolean isDraggingScrollBar)
    {
        super.UpdateScrolling(isDraggingScrollBar);
        int rowCount = GetRowCount();
        int prevRow = currentRow;
        currentRow = (int) Mathf.Clamp(scrollBar.currentScrollPercent * rowCount, 0, rowCount - 1);

        // Reset zooming of the cards newly added to the screen
        int min;
        int max;
        if (prevRow < currentRow) {
            min = (prevRow + visibleRowCount) * rowSize;
            max = min + (currentRow - prevRow) * rowSize;
        }
        else if (currentRow < prevRow) {
            max = prevRow * rowSize;
            min = max - (prevRow - currentRow) * rowSize;
        }
        else {
            return;
        }

        for (int i = Math.max(0, min); i < Math.min(max, relicGroup.size()); i++) {
            RelicInfo card = relicGroup.get(i);
            card.relic.scale = target_scale;
        }

    }

    public int GetRowCount() {
        return (relicGroup.size() - 1) / rowSize;
    }

}