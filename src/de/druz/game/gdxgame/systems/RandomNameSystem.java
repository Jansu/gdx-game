package de.druz.game.gdxgame.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;

import de.druz.game.gdxgame.components.RandomName;
import de.druz.game.gdxgame.utils.RandomUtils;

	
public class RandomNameSystem extends EntitySystem {
	private static final String NOUN = "#N";
	private static final String ADJECTIVE = "#adj";
	
	private String[] nouns;
	private String[] adjectives;
	private String[] patterns;
	private String[] surnames;
	private String[] maleNames;
	private String[] femaleNames;

	@Mapper
	ComponentMapper<RandomName> em;

	
	public RandomNameSystem() {
		super(Aspect.getAspectForAll(RandomName.class));
	}
	
	@Override
	protected void initialize() {
		String noun = Gdx.files.internal("data/text/en/noun.txt").readString("utf-8");
		String adj = Gdx.files.internal("data/text/en/adj.txt").readString("utf-8");
		String pattern = Gdx.files.internal("data/text/en/pattern.txt").readString("utf-8");
		String surname = Gdx.files.internal("data/text/names/surnames.txt").readString("utf-8");
		String maleName = Gdx.files.internal("data/text/names/malenames.txt").readString("utf-8");
		String femaleName = Gdx.files.internal("data/text/names/femalenames.txt").readString("utf-8");
		nouns = noun.replaceAll("//(.*?)\n", "").split("\n");
		adjectives = adj.replaceAll("//(.*?)\n", "").split("\n");
		patterns = pattern.replaceAll("//(.*?)\n", "").split("\n");
		surnames = surname.replaceAll("//(.*?)\n", "").split("\n");
		maleNames = maleName.replaceAll("//(.*?)\n", "").split("\n");
		femaleNames = femaleName.replaceAll("//(.*?)\n", "").split("\n");
	}

	@Override
	protected void inserted(Entity e) {
		RandomName name = em.get(e);
		switch (name.type) {
		case GAME:
			name.name = generateGameName();
			break;
		case MALE:
			name.name = RandomUtils.random(maleNames) + " " + RandomUtils.random(surnames);
			break;
		case FEMALE:
			name.name = RandomUtils.random(femaleNames) + " " + RandomUtils.random(surnames);
			break;
		default:
			break;
		}
	}

	private String generateGameName() {
		StringBuilder result = new StringBuilder();
		String pattern = RandomUtils.random(patterns);
//		System.out.println("pattern " + pattern);
		for (String part : pattern.split(" ")) {
			result.append(replacePlaceHolders(part)).append(" ");
		}
//		System.out.println(result.toString());
		return beautifyGameName(result.toString());
	}

	private String beautifyGameName(String string) {
		// add space after comma
		string = string.replaceAll(",", ", ");
		// "an" not "a" before word starting with vocal
		string = string.replaceAll(" a a", " an a");
		string = string.replaceAll(" a A", " an A");
		string = string.replaceAll(" a e", " an e");
		string = string.replaceAll(" a E", " an E");
		string = string.replaceAll(" a i", " an i");
		string = string.replaceAll(" a I", " an I");
		string = string.replaceAll(" a o", " an o");
		string = string.replaceAll(" a O", " an O");
		string = string.replaceAll(" a u", " an u");
		string = string.replaceAll(" a U", " an U");
		// No double spaces
		string = string.replaceAll(" +", " ");
		// first letter capital
		string = string.substring(0, 1).toUpperCase() + string.substring(1); 
		return string;
	}

	private String replacePlaceHolders(String part) {
		if (part.contains(NOUN)) {
			return part.replaceAll(NOUN, RandomUtils.random(nouns));
		} else if (part.contains(ADJECTIVE)) {
			return part.replaceAll(ADJECTIVE, RandomUtils.random(adjectives));
		} else {
			return part;
		}
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		
	}

	@Override
	protected boolean checkProcessing() {
		return false;
	}
}
