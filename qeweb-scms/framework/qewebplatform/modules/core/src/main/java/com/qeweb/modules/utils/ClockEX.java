package com.qeweb.modules.utils;

import java.sql.Timestamp;
import java.util.Date;

import org.springside.modules.utils.Clock;




public interface ClockEX extends Clock{
	static final ClockEX DEFAULT = new DefaultClock();

	@Override
	Date getCurrentDate();

	@Override
	long getCurrentTimeInMillis();
	
	Timestamp getCurrentStamp();

	/**
	 * 默认时间提供者，返回当前的时间，线程安全。
	 */
	public static class DefaultClock implements ClockEX {

		@Override
		public Date getCurrentDate() {
			return new Date();
		}

		@Override
		public long getCurrentTimeInMillis() {
			return System.currentTimeMillis();
		}

		@Override
		public Timestamp getCurrentStamp() {
			return new Timestamp(getCurrentTimeInMillis());
		}

		
	}

	/**
	 * 可配置的时间提供者，用于测试.
	 */
	public static class MockClock implements ClockEX {

		private long time;

		public MockClock() {
			this(0);
		}

		public MockClock(Date date) {
			this.time = date.getTime();
		}

		public MockClock(long time) {
			this.time = time;
		}

		@Override
		public Date getCurrentDate() {
			return new Date(time);
		}

		@Override
		public long getCurrentTimeInMillis() {
			return time;
		}

		/**
		 * 重新设置日期。
		 */
		public void update(Date newDate) {
			time = newDate.getTime();
		}

		/**
		 * 重新设置时间。
		 */
		public void update(long newTime) {
			this.time = newTime;
		}

		/**
		 * 滚动时间.
		 */
		public void increaseTime(int millis) {
			time += millis;
		}

		/**
		 * 滚动时间.
		 */
		public void decreaseTime(int millis) {
			time -= millis;
		}

		@Override
		public Timestamp getCurrentStamp() {
			return new Timestamp(getCurrentTimeInMillis());
		}
	}

}

