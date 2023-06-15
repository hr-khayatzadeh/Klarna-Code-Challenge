package org.example;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Transactions {

	public static class Transaction {

		private String firstName;
		private String lastName;
		private String email;
		private Integer amount;
		private String transactionId;

		public Transaction(String firstName, String lastName, String email, Integer amount, String transactionId) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
			this.amount = amount;
			this.transactionId = transactionId;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public Integer getAmount() {
			return amount;
		}

		public void setAmount(Integer amount) {
			this.amount = amount;
		}

		public String getTransactionId() {
			return transactionId;
		}

		public void setTransactionId(String transactionId) {
			this.transactionId = transactionId;
		}
	}

	public static List<String> findRejectedTransactions(final List<String> transactions, final int creditLimit) {
		if (transactions.isEmpty()) {
			return List.of();
		}
		final var trans = transactions.stream().map(t -> {
			final var toArray = Arrays.asList(t.split(","));
			try {
				return new Transaction(toArray.get(0), toArray.get(1), toArray.get(2), Integer.valueOf(toArray.get(3)), toArray.get(4));
			} catch (Exception e) {
				throw new RuntimeException(String.format(
						"All the required parameters must be set for the transaction, Trans: %s",
						transactions
				));
			}
		}).toList();
		var approvedCreditSum = new AtomicInteger(trans.get(0).getAmount());
		return trans.stream().map(t -> {
					if (approvedCreditSum.get() <= creditLimit) {
						approvedCreditSum.set(Integer.sum(approvedCreditSum.get(), t.amount));
						return null;
					} else {
						return t.getTransactionId();
					}
				}
		).filter(Objects::nonNull).toList();
	}

}
