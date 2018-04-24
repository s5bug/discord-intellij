package com.tsunderebug.discordintellij;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "presenceEnabled", storages = {@Storage(value = "workspace.xml")})
public class PresenceEnabled implements PersistentStateComponent<PresenceEnabled> {

	private boolean enabled;

	public PresenceEnabled() {
		this(true);
	}

	public PresenceEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Nullable
	@Override
	public PresenceEnabled getState() {
		return this;
	}

	@Override
	public void loadState(@NotNull PresenceEnabled presenceEnabled) {
		this.enabled = presenceEnabled.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}
}
