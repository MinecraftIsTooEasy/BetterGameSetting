package moddedmite.xylose.bettergamesetting.api;

public interface ICreateWorld {
	default void switchSkillsEnable() {
	}

	default boolean isSkillsEnable() {
		return false;
	}
}
