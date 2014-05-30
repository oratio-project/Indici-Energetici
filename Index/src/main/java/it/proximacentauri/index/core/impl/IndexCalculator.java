/*******************************************************************************
 * This file is part of Oratio4Energy.
 * 
 * Oratio4Energy is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Oratio4Energy is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Oratio4Energy.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
package it.proximacentauri.index.core.impl;

import it.proximacentauri.index.domain.BIndex;
import it.proximacentauri.index.domain.BIndexSnap;
import it.proximacentauri.index.domain.BalanceSnapshot;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import org.mbertoli.jfep.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.ExpressionBuilder;
import de.congrace.exp4j.UnknownFunctionException;
import de.congrace.exp4j.UnparsableExpressionException;

/**
 * Class for real index calculation
 * 
 * @author proximacentauri
 * 
 */
class IndexCalculator {

	final private Logger log = LoggerFactory.getLogger(IndexCalculator.class);

	BIndexSnap calculateIndex(BIndex index, Map<String, BalanceSnapshot> snapshots, Map<String, BigDecimal> depValue) {
		log.debug("calculate index for {}", index);

		// create formula
		log.trace("build formula {}", index.getFormula());
		final ExpressionBuilder expr = new ExpressionBuilder(index.getFormula());

		// work in arguments
		for (String arg : index.getArguments()) {
			// get value from snapshost
			if (snapshots.containsKey(arg)) {
				final BigDecimal valueBigDecimal = snapshots.get(arg).getValue();
				expr.withVariable(arg, valueBigDecimal.doubleValue());
				log.trace("add aguments {} with values {}", arg, valueBigDecimal);
			}

			// get value from depends
			if (depValue.containsKey(arg)) {
				final BigDecimal valueBigDecimal = depValue.get(arg);
				expr.withVariable(arg, valueBigDecimal.doubleValue());
				log.trace("add dep value aguments {} with values {}", arg, valueBigDecimal);
			}
		}

		Calculable calculable;
		try {
			calculable = expr.build();
			final double result = calculable.calculate();
			log.debug("result for formula {}, is {}", index.getFormula(), result);

			// create the index snapshots
			BIndexSnap indexSnap = new BIndexSnap();
			indexSnap.setIndex(index);

			// copy the formula
			indexSnap.setFormula(index.getFormula());
			indexSnap.setValue(new BigDecimal(result));
			indexSnap.setArguments(index.getArguments());

			return indexSnap;
		} catch (UnknownFunctionException e) {
			e.printStackTrace();
		} catch (UnparsableExpressionException e) {
			e.printStackTrace();
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	Set<String> parseFormula(String formula) {
		log.debug("Try to parse the formula {}", formula);
		// add parser formula
		Parser parser = new Parser(formula);
		return parser.getParsedVariables();
	}
}
