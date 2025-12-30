//
//  ChargeViewController.h
//  BJMainApp
//
//  Created by KimOC on 7/29/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface Drv_ChargeViewController : SuperViewController <TradeSvcDelegate>
{
    /********************** UI Controls **********************/
    IBOutlet UILabel *          _lblLeftAccum;
    IBOutlet UITextField *      _txtChargeVal;
    
    /************************ Member Variable ****************/
    double                      mLeftMoney;
}

- (IBAction)onClickedBack:(id)sender;

- (IBAction)onClickedNext:(id)sender;

- (IBAction)onClickedMenuAccum:(id)sender;
- (IBAction)onClickedMenuWithdraw:(id)sender;

@end
