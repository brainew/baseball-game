package service.impl;

import model.BaseballGameModel;
import service.BaseballGameService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Scanner;

public class BaseballGameServiceImpl implements BaseballGameService {
	private BaseballGameModel baseballGame;

	public BaseballGameServiceImpl(BaseballGameModel baseballGame) {
		this.baseballGame = baseballGame;
	}

	@Override
	public void startGame() {
		createRandomNumbers();
		System.out.println("숫자를입력해주세요: ");
		getInputNumber();
		checkBallStrike();
	}

	@Override
	public void createRandomNumbers() {
		LinkedHashSet<Integer> linkedHashMap = new LinkedHashSet<>();

		while (linkedHashMap.size() < 3) {
			linkedHashMap.add(((int) (Math.random() * 9) + 1));
		}

		linkedHashMap.iterator().forEachRemaining(baseballGame::addDefaultNumbers);
	}

	@Override
	public void getInputNumber() {
		Scanner scanner = new Scanner(System.in);
		String inputNumber = scanner.nextLine();
		try {
			validInputNumbers(inputNumber);
			baseballGame.setUserInputNumbers(splitStringToArray(inputNumber));
		} catch (Exception e) {
			System.out.println("ERROR: 자리수와 숫자를 확인하시고 다시 입력해주세요");
			getInputNumber();
		}
	}

	@Override
	public ArrayList<Integer> splitStringToArray(String inputNumber) {
		ArrayList<Integer> convertArray = new ArrayList<>();
		for (char number : inputNumber.toCharArray()) {
			convertArray.add(convertStringToInteger(number));
		}

		return convertArray;
	}

	@Override
	public Integer convertStringToInteger(char input) {
		return Integer.parseInt(String.valueOf(input));
	}

	@Override
	public void validInputNumbers(String inputNumber) throws Exception {
		if (inputNumber.length() != 3) {
			throw new Exception("wrong input number size");
		}

		for (char value : inputNumber.toCharArray()) {
			checkRangeByInputNumber(Integer.parseInt(String.valueOf(value)));
		}
	}

	@Override
	public void checkRangeByInputNumber(int inputNumber) throws Exception {
		if (inputNumber < 1 || inputNumber > 9) {
			throw new Exception("wrong input number range");
		}
	}

	@Override
	public void checkBallStrike() {
		baseballGame.setBall(isBall(baseballGame.getDefaultNumbers(), baseballGame.getUserInputNumbers()));
		baseballGame.setStrike(isStrike(baseballGame.getDefaultNumbers(), baseballGame.getUserInputNumbers()));

		presentGameResult();
	}

	@Override
	public void presentGameResult() {
		System.out.println(baseballGame.getStrike() + " 스트라이크 " + baseballGame.getBall() + "볼");

		if (baseballGame.getStrike() != 3) {
			startRetryGame();
			return;
		}

		clearGame();
	}

	@Override
	public void clearGame() {
		System.out.println("3개의 숫자를 모두 맞히셨습니다! 게임종료");
		System.out.println("게임을 새로 시작하려면 1, 종료하려면 2를 입력하세요.");
		Scanner scanner = new Scanner(System.in);
		String inputNumber = scanner.nextLine();
	}

	@Override
	public int isBall(ArrayList<Integer> defaultNumbers, ArrayList<Integer> inputNumbers) {
		int size = defaultNumbers.size();
		int ballCount = 0;

		for (int i = 0; i < size; i++) {
			ArrayList<Integer> copyArray = arrayDeepCopy(defaultNumbers, i);
			ballCount += (copyArray.contains(inputNumbers.get(i))) ? 1 : 0;
		}

		return ballCount;
	}

	@Override
	public int isStrike(ArrayList<Integer> defaultNumbers, ArrayList<Integer> inputNumbers) {
		int strikeCount = 0;

		for (int defaultNumber: defaultNumbers) {
			strikeCount += (inputNumbers.get(defaultNumbers.indexOf(defaultNumber)).equals(defaultNumber)) ? 1 : 0;
		}

		return strikeCount;
	}

	@Override
	public void startRetryGame() {
		System.out.println("숫자를입력해주세요: ");
		getInputNumber();
		checkBallStrike();
	}

	private ArrayList<Integer> arrayDeepCopy(ArrayList<Integer> baseArray, int removeIndex) {
		Integer[] copyArray = new Integer[baseArray.size()];
		System.arraycopy(baseArray.toArray(), 0, copyArray, 0, baseArray.size());
		ArrayList<Integer> resultArray = new ArrayList<>(Arrays.asList(copyArray));
		removeIndex(resultArray, removeIndex);

		return resultArray;
	}

	private void removeIndex(ArrayList<Integer> array, int removeIndex) {
		array.remove(removeIndex);
	}
}
