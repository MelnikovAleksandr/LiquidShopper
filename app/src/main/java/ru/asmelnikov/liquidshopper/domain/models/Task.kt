package ru.asmelnikov.liquidshopper.domain.models

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
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
    val sumPrice: Int,
    val items: List<Item>
): Parcelable

@Parcelize
data class Item(
    val uid: Int,
    val timeStamp: LocalDateTime,
    val taskId: Int,
    val itemName: String,
    val count: Int,
    val price: Int,
    val units: UnitType,
    val bought: Boolean
): Parcelable

enum class TaskTypes(@StringRes val stringRes: Int, @DrawableRes val drawableRes: Int, val color: Color) {
    PHARMACY(R.string.category_pharmacy, R.drawable.medicine_ic, Color(0xFF533545)),
    ALCOHOL(R.string.category_alcohol, R.drawable.alcohol_ic, Color(0xFFFF9060)),
    GROCERIES(R.string.category_groceries, R.drawable.food_ic, Color(0xFFFFB957)),
    SUPERMARKET(R.string.category_supermarket, R.drawable.mall_ic, Color(0xFFFFD7D7)),
    COSMETICS(R.string.category_cosmetics, R.drawable.cosmetic_ic, Color(0xFFBB404E)),
    HOUSEHOLD(R.string.category_household, R.drawable.cleaning_products_ic, Color(0xFFFFDE52)),
    PETS(R.string.category_pets, R.drawable.pet_ic, Color(0xFFA0C394)),
    GARDEN(R.string.category_garden, R.drawable.botany_ic, Color(0xFF72AF5F)),
    CLOTHING(R.string.category_clothing, R.drawable.clothes_ic, Color(0xFFE6706C)),
    BOOKS(R.string.category_books, R.drawable.books_ic_2, Color(0xFF5EB3DD)),
    TOOLS(R.string.category_tools, R.drawable.tools_ic, Color(0xFFE1E1E5)),
    DELIVERY(R.string.category_delivery, R.drawable.delivery_ic, Color(0xFFF0C48A)),
    APPLIANCES(R.string.category_appliances, R.drawable.tech_ic, Color(0xFF6E6E96)),
    FURNITURE(R.string.category_furniture, R.drawable.furniture_ic, Color(0xFF453F3C)),
    SPORTS(R.string.category_sports, R.drawable.sports_ic, Color(0xFFF5AB33)),
    CHILDREN(R.string.category_children, R.drawable.baby_ic, Color(0xFF2FACA9)),
    AUTOMOTIVE(R.string.category_automotive, R.drawable.car_ic, Color(0xFF6E6E96)),
    OTHER(R.string.category_other, R.drawable.other_items_ic, Color(0xFF3594BF))
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