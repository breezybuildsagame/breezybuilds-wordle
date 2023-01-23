//
//  ColorTests.swift
//  iosAppTests
//
//  Created by Bradley Phillips on 1/9/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import XCTest
@testable import iosApp

final class ColorTests: XCTestCase {

    func test_when_getting_custom_colors__expected_rgba_values_are_set()
    {
        getColorsToTest().forEach
        { color in
            [UIUserInterfaceStyle.light, UIUserInterfaceStyle.dark].forEach()
            { style in
                let colorName = color.name
                let theme = UITraitCollection(userInterfaceStyle: style)
                
                let expectedRed: CGFloat = style == .light ? color.lightR : color.darkR
                let expectedGreen: CGFloat = style == .light ? color.lightG : color.darkG
                let expectedBlue: CGFloat = style == .light ? color.lightB : color.darkB
                let expectedAlpha: CGFloat = style == .light ? color.lightA : color.darkA
                
                guard let sut = UIColor(named: colorName)?.resolvedColor(with: theme).rgba else {
                    return XCTFail("Color set \"\(colorName)\" does not exist in assets.")
                }
                
                let actual = self.convertValues(
                    red: sut.red, green: sut.green, blue: sut.blue, alpha: sut.alpha
                )
                
                let themeString = style == .light ? "light" : "dark"
                XCTAssertEqual(expectedRed, actual.red, "\(colorName) \(themeString): red")
                XCTAssertEqual(expectedGreen, actual.green, "\(colorName) \(themeString): green")
                XCTAssertEqual(expectedBlue, actual.blue, "\(colorName) \(themeString): blue")
                XCTAssertEqual(expectedAlpha, actual.alpha, accuracy: 0.05, "\(colorName) \(themeString): alpha")
            }
        }
    }
        
    private func getColorsToTest() -> [TestColor]
    {
        return [
            TestColor(
                name: "color.primary",
                lightR: 32, lightG: 32, lightB: 32, lightA: 1,
                darkR: 32, darkG: 32, darkB: 32, darkA: 1
            ),
            TestColor(
                name: "color.on_primary",
                lightR: 255, lightG: 255, lightB: 255, lightA: 1,
                darkR: 255, darkG: 255, darkB: 255, darkA: 1
            )
        ]
    }
    
    private func convertValues(
        red: CGFloat, green: CGFloat, blue: CGFloat, alpha: CGFloat
    ) -> (red: CGFloat, green: CGFloat, blue: CGFloat, alpha: CGFloat) {
        let redValue: CGFloat = red * 255
        let greenValue: CGFloat = green * 255
        let blueValue: CGFloat = blue * 255
        let alphaValue: CGFloat = alpha
        return (redValue, greenValue, blueValue, alphaValue)
    }
    
    struct TestColor
    {
        let name: String
        
        let lightR: CGFloat
        let lightG: CGFloat
        let lightB: CGFloat
        let lightA: CGFloat
        
        let darkR: CGFloat
        let darkG: CGFloat
        let darkB: CGFloat
        let darkA: CGFloat
    }
}
