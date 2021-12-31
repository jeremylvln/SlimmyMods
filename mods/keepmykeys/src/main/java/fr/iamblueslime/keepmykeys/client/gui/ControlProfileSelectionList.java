package fr.iamblueslime.keepmykeys.client.gui;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.iamblueslime.keepmykeys.KeepMyKeys;
import fr.iamblueslime.keepmykeys.logic.ControlProfile;
import fr.iamblueslime.keepmykeys.logic.ControlProfileRegistry;
import fr.iamblueslime.keepmykeys.logic.KeyBindMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class ControlProfileSelectionList extends ContainerObjectSelectionList<ControlProfileSelectionList.Entry> {
    public static final TranslatableComponent NO_PROFILE_FOUND_MESSAGE = new TranslatableComponent("options.keepmykeys.importselectprofile.list.empty");
    public static final String MATCHING_KEYS_MESSAGE_KEY = "options.keepmykeys.importselectprofile.list.matchingkeys";
    public static final String PROFILE_LAST_MODIFIED_ON_MESSAGE_KEY = "options.keepmykeys.importselectprofile.list.last_modified_on";
    public static final String PROFILE_FILE_PATH_MESSAGE_KEY = "options.keepmykeys.importselectprofile.list.file_path";

    private final ImportSelectProfileScreen screen;

    private List<ControlProfile> controlProfiles;
    private List<ControlProfile> ignoredControlProfiles;
    private KeyBindMap keyBindMap;

    public ControlProfileSelectionList(ImportSelectProfileScreen screen, Minecraft minecraft, int width, int height, int y0, int y1) {
        super(minecraft, width, height, y0, y1, 28);
        this.screen = screen;
    }

    public void refreshControlProfiles() {
        this.controlProfiles = new ArrayList<>(ControlProfileRegistry.INSTANCE.lookupForProfiles());
        this.ignoredControlProfiles = new ArrayList<>();
        this.rebuildKeyBindMap();
    }

    private void rebuildKeyBindMap() {
        this.clearEntries();

        if (this.controlProfiles.isEmpty() && this.ignoredControlProfiles.isEmpty()) {
            this.addEntry(new CategoryEntry(NO_PROFILE_FOUND_MESSAGE));
            return;
        }

        this.keyBindMap = ControlProfileRegistry.INSTANCE.createKeyBindMapFromProfiles(this.controlProfiles);

        List<ControlProfile> ignoredControlProfiles = this.keyBindMap.getControlProfilesWithoutMatch();
        this.ignoredControlProfiles.addAll(ignoredControlProfiles);
        this.controlProfiles.removeAll(ignoredControlProfiles);

        this.addEntry(new CategoryEntry(new TextComponent("Profiles to apply")));
        this.controlProfiles.forEach((controlProfile) -> this.addEntry(new ProfileEntry(
                controlProfile,
                new ControlProfile.Stats(this.keyBindMap.getMatchingKeysForProfile(controlProfile))
        )));

        if (!this.ignoredControlProfiles.isEmpty()) {
            this.addEntry(new CategoryEntry(new TextComponent("Ignored profiles")));
            this.ignoredControlProfiles.forEach((controlProfile) -> this.addEntry(new ProfileEntry(controlProfile, null)));
        }

        this.screen.updateButtonStatus(!this.controlProfiles.isEmpty());
    }

    private void considerControlProfile(ControlProfile controlProfile) {
        this.controlProfiles.add(controlProfile);
        this.ignoredControlProfiles.remove(controlProfile);
        this.rebuildKeyBindMap();
    }

    private void ignoreControlProfile(ControlProfile controlProfile) {
        this.ignoredControlProfiles.add(controlProfile);
        this.controlProfiles.remove(controlProfile);
        this.rebuildKeyBindMap();
    }

    public int getRowWidth() {
        return super.getRowWidth() + 32;
    }

    public KeyBindMap getKeyBindMap() {
        return this.keyBindMap;
    }

    @Override
    protected boolean isFocused() {
        return this.screen.getFocused() == this;
    }

    protected abstract static class Entry extends ContainerObjectSelectionList.Entry<ControlProfileSelectionList.Entry> {
    }

    private class CategoryEntry extends Entry {
        private final Component name;
        private final int width;

        public CategoryEntry(Component name) {
            this.name = name;
            this.width = ControlProfileSelectionList.this.minecraft.font.width(this.name);
        }

        @Override
        public void render(PoseStack pPoseStack, int pIndex, int pTop, int pLeft, int pWidth, int pHeight, int pMouseX, int pMouseY, boolean pIsMouseOver, float pPartialTick) {
            ControlProfileSelectionList.this.minecraft.font.draw(pPoseStack, this.name, (float) (ControlProfileSelectionList.this.minecraft.screen.width / 2 - this.width / 2), (float) (pTop + pHeight - 12), 16777215);
        }

        @Override
        public boolean changeFocus(boolean pFocus) {
            return false;
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return Collections.emptyList();
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return ImmutableList.of(new NarratableEntry() {
                public NarratableEntry.NarrationPriority narrationPriority() {
                    return NarratableEntry.NarrationPriority.HOVERED;
                }

                public void updateNarration(NarrationElementOutput narrationElementOutput) {
                    narrationElementOutput.add(NarratedElementType.TITLE, ControlProfileSelectionList.CategoryEntry.this.name);
                }
            });
        }
    }

    private class ProfileEntry extends Entry {
        private static final DateFormat DATE_FORMAT = new SimpleDateFormat();
        private static final ResourceLocation ICON_LOCATION = new ResourceLocation(KeepMyKeys.MODID, "textures/gui/widgets.png");

        private final ControlProfile controlProfile;
        private final ControlProfile.Stats controlProfileStats;
        private final TranslatableComponent matchingKeysMessage;
        private final Button toggleButton;

        public ProfileEntry(ControlProfile controlProfile, ControlProfile.Stats controlProfileStats) {
            this.controlProfile = controlProfile;
            this.controlProfileStats = controlProfileStats;
            this.matchingKeysMessage = new TranslatableComponent(MATCHING_KEYS_MESSAGE_KEY,
                    controlProfileStats != null ? controlProfileStats.matchingKeyBinds() : 0);

            this.toggleButton = new Button(0, 0, 20, 20, TextComponent.EMPTY, (button) -> {
                if (this.controlProfileStats != null) {
                    ControlProfileSelectionList.this.ignoreControlProfile(this.controlProfile);
                } else {
                    ControlProfileSelectionList.this.considerControlProfile(this.controlProfile);
                }
            });
        }

        @Override
        public void render(PoseStack pPoseStack, int pIndex, int pTop, int pLeft, int pWidth, int pHeight, int pMouseX, int pMouseY, boolean pIsMouseOver, float pPartialTick) {
            boolean isIgnored = this.controlProfileStats == null;

            ControlProfileSelectionList.this.minecraft.font.draw(pPoseStack, this.controlProfile.title(), (float) (pLeft + 16 + 8), (float) (pTop + 1), 16777215);
            ControlProfileSelectionList.this.minecraft.font.draw(pPoseStack, this.matchingKeysMessage, (float) (pLeft + 16 + 8), (float) (pTop + 9 + 3), !isIgnored ? 0x99f76e : 8421504);

            this.toggleButton.setMessage(new TextComponent(!isIgnored ? "-" : "+"));
            this.toggleButton.x = pLeft + pWidth - 20;
            this.toggleButton.y = pTop;
            this.toggleButton.render(pPoseStack, pMouseX, pMouseY, pPartialTick);

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, ICON_LOCATION);
            RenderSystem.enableBlend();
            GuiComponent.blit(pPoseStack, pLeft, pTop, !isIgnored ? 0 : 16, 0, 16, 16, 128, 128);
            RenderSystem.disableBlend();

            if (this.toggleButton.isHoveredOrFocused()) {
                ControlProfileSelectionList.this.screen.setToolTip(Arrays.asList(
                        new TranslatableComponent(PROFILE_LAST_MODIFIED_ON_MESSAGE_KEY, DATE_FORMAT.format(new Date(this.controlProfile.file().lastModified()))),
                        new TranslatableComponent(PROFILE_FILE_PATH_MESSAGE_KEY, this.controlProfile.file().getAbsolutePath())
                ));
            }
        }

        @Override
        public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
            return this.toggleButton.mouseClicked(pMouseX, pMouseY, pButton);
        }

        @Override
        public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
            return this.toggleButton.mouseReleased(pMouseX, pMouseY, pButton);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return Collections.emptyList();
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return Collections.emptyList();
        }
    }
}
