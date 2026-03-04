package ru.asmelnikov.liquidshopper.domain.models

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import ru.asmelnikov.liquidshopper.R
import java.time.LocalDate
import java.time.LocalDateTime

@Parcelize
data class GroupedTasksByDay(
    val start: LocalDate,
    val tasksCount: Int,
    val tasks: List<Task>
): Parcelable

@Parcelize
data class Task(
    val uid: Int,
    val taskName: String,
    val taskType: TaskTypes,
    val timeStamp: LocalDateTime,
    val isCompleted: Boolean,
    val allItemsCount: Int,
    val inProgressItemsCount: Int,
    val items: List<Item>
): Parcelable

@Parcelize
data class Item(
    val uid: Int,
    val taskId: Int,
    val itemName: String,
    val count: Int,
    val price: Int,
    val units: UnitType,
    val bought: Boolean
): Parcelable

enum class TaskTypes(@StringRes val stringRes: Int, @DrawableRes val drawableRes: Int) {
    PHARMACY(R.string.category_pharmacy, R.drawable.medicine_ic),
    ALCOHOL(R.string.category_alcohol, R.drawable.alcohol_ic),
    GROCERIES(R.string.category_groceries, R.drawable.food_ic),
    SUPERMARKET(R.string.category_supermarket, R.drawable.mall_ic),
    COSMETICS(R.string.category_cosmetics, R.drawable.cosmetic_ic),
    HOUSEHOLD(R.string.category_household, R.drawable.cleaning_products_ic),
    PETS(R.string.category_pets, R.drawable.pet_ic),
    GARDEN(R.string.category_garden, R.drawable.botany_ic),
    CLOTHING(R.string.category_clothing, R.drawable.clothes_ic),
    BOOKS(R.string.category_books, R.drawable.books_ic_2),
    TOOLS(R.string.category_tools, R.drawable.tools_ic),
    DELIVERY(R.string.category_delivery, R.drawable.delivery_ic),
    APPLIANCES(R.string.category_appliances, R.drawable.tech_ic),
    FURNITURE(R.string.category_furniture, R.drawable.furniture_ic),
    SPORTS(R.string.category_sports, R.drawable.sports_ic),
    CHILDREN(R.string.category_children, R.drawable.baby_ic),
    AUTOMOTIVE(R.string.category_automotive, R.drawable.car_ic),
    OTHER(R.string.category_other, R.drawable.other_items_ic)
}

enum class UnitType(@StringRes val stringRes: Int) {
    PIECES(R.string.unit_pcs),
    KILOGRAM(R.string.unit_kg),
    GRAM(R.string.unit_g),
    TON(R.string.unit_t),
    LITER(R.string.unit_l),
    MILLILITER(R.string.unit_ml),
    BOTTLE(R.string.unit_bottle),
    PACK(R.string.unit_pack),
    BOX(R.string.unit_box),
    PACKET(R.string.unit_packet),
    SET(R.string.unit_set),
    PAIR(R.string.unit_pair),
    SHEET(R.string.unit_sheet),
    ROLL(R.string.unit_roll),
    METER(R.string.unit_m),
    SQUARE_METER(R.string.unit_m2),
    CUBIC_METER(R.string.unit_m3),
    CENTIMETER(R.string.unit_cm),
    SQUARE_CENTIMETER(R.string.unit_cm2),
    CUBIC_CENTIMETER(R.string.unit_cm3),
    LINEAR_METER(R.string.unit_linear_m),
    HOUR(R.string.unit_hour),
    DAY(R.string.unit_day),
    MINUTE(R.string.unit_minute)
}