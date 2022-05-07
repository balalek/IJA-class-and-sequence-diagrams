/**
 * @author Martin Baláž
 */
package com.javaprojekt;

/**
 * Enum, který obsahuje typy operací, z kterých se lze vrátit (undo)
 */
enum operation {
    REMOVE,
    CREATE,
    RENAME,
    CHANGE,
    DRAG
}
