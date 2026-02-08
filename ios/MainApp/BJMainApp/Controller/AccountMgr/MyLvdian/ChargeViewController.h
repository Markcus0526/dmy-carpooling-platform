//
//  ChargeViewController.h
//  BJMainApp
//
//  Created by KimOC on 7/29/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ChargeViewController : UIViewController <TradeSvcDelegate>
{
    /********************** UI Controls **********************/
    IBOutlet UILabel *          _lblLeftAccum;
    IBOutlet UITextField *      _txtChargeVal;
    
    /************************ Member Variable ****************/
    double                      mLeftMoney;
}

- (void) updateData;

- (IBAction)onClickedBack:(id)sender;
- (IBAction)onClickedNext:(id)sender;


@end
