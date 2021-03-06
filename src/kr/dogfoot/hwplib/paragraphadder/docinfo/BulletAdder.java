package kr.dogfoot.hwplib.paragraphadder.docinfo;

import kr.dogfoot.hwplib.object.docinfo.Bullet;
import kr.dogfoot.hwplib.object.docinfo.numbering.ParagraphHeadInfo;

public class BulletAdder {
	private DocInfoAdder docInfoAdder;

	public BulletAdder(DocInfoAdder docInfoAdder) {
		this.docInfoAdder = docInfoAdder;
	}

	public int processById(int sourceId) {
		Bullet source = docInfoAdder.getSourceHWPFile().getDocInfo().getBulletList().get(sourceId);
		int index = findFromTarget(source);
		if (index == -1) {
			index = addAndCopy(source);
		}
		return index;
	}

	private int findFromTarget(Bullet source) {
		int count = docInfoAdder.getTargetHWPFile().getDocInfo().getBulletList().size();
		for (int index = 0; index < count; index++) {
			Bullet target = docInfoAdder.getTargetHWPFile().getDocInfo().getBulletList().get(index);
			if (equal(source, target)) {
				return index;
			}
		}
		return -1;
	}

	private boolean equal(Bullet source, Bullet target) {
		if (equalParagraphHeadInfo(source.getParagraphHeadInfo(), target.getParagraphHeadInfo())
				&& source.getBulletChar().equals(target.getBulletChar())) {
			return true;
		}
		return false;
	}

	private boolean equalParagraphHeadInfo(ParagraphHeadInfo source, ParagraphHeadInfo target) {
		if (source.getProperty().getValue() == target.getProperty().getValue()
				&& source.getCorrectionValueForWidth() == target.getCorrectionValueForWidth()
				&& source.getDistanceFromBody() == target.getDistanceFromBody() && docInfoAdder.forCharShape()
						.equalById((int) source.getCharShapeID(), (int) target.getCharShapeID())) {
			return true;
		}
		return false;
	}

	private int addAndCopy(Bullet source) {
		Bullet target = docInfoAdder.getTargetHWPFile().getDocInfo().addNewBullet();
		copyParagraphHeadInfo(source.getParagraphHeadInfo(), target.getParagraphHeadInfo());
		source.setBulletChar(target.getBulletChar());

		return docInfoAdder.getTargetHWPFile().getDocInfo().getBulletList().size() - 1;
	}

	private void copyParagraphHeadInfo(ParagraphHeadInfo source, ParagraphHeadInfo target) {
		target.getProperty().setValue(source.getProperty().getValue());
		target.setCorrectionValueForWidth(source.getCorrectionValueForWidth());
		target.setDistanceFromBody(source.getDistanceFromBody());
		target.setCharShapeID(docInfoAdder.forCharShape().processById((int) target.getCharShapeID()));
	}

	public boolean equalById(int sourceId, int targetId) {
		Bullet source = docInfoAdder.getSourceHWPFile().getDocInfo().getBulletList().get(sourceId);
		Bullet target = docInfoAdder.getTargetHWPFile().getDocInfo().getBulletList().get(targetId);
		return equal(source, target);
	}
}
