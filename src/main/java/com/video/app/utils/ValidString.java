package com.video.app.utils;

import com.ibm.icu.text.Transliterator;

public class ValidString {
    public static String slugify(String input) {
//        String slug = input.toLowerCase()
//                // Loại bỏ các ký tự không phải chữ cái, số hoặc dấu cách
//                .replaceAll("[^a-z0-9\\s]", "")
//                // Thay thế các dấu cách bằng dấu gạch ngang
//                .replaceAll("\\s+", "-")
//                // Loại bỏ các dấu gạch ngang liên tiếp
//                .replaceAll("-+", "-");
//        // Loại bỏ các dấu gạch ngang ở đầu và cuối chuỗi
//        slug = slug.replaceAll("^-|-$", "");
        Transliterator transliterator = Transliterator.getInstance("NFD; [:Nonspacing Mark:] Remove; Latin; Lower;");
        String latinText = transliterator.transliterate(input);
        return latinText.toLowerCase().trim()
                .replaceAll("\\s+", "-");
    }
}
