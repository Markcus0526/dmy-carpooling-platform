//
//  Cus_SuccessShortOrderViewController.m
//  BJPinChe
//
//  Created by Kim Ok Chol on 9/11/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//  乘客 单次拼车  出单成功界面
// 未完成 车牌号 
#import "Cus_SuccessShortOrderViewController.h"
#import "Cus_MainTabViewController.h"

@interface Cus_SuccessShortOrderViewController ()

@end

@implementation Cus_SuccessShortOrderViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
	
	[self initControls];
}


-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    UIScreen *screen = [UIScreen mainScreen];
    if (screen.bounds.size.height>500) {
        myscrollView.contentSize = CGSizeMake(320, 0);
    }
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (void) initControls
{
	[_imgUser setImageWithURL:[NSURL URLWithString:self.mDriverInfo.image] placeholderImage:[UIImage imageNamed:@"stOrderImage.png"]];
	[_imgCar setImageWithURL:[NSURL URLWithString:self.mDriverInfo.carimg] placeholderImage:[UIImage imageNamed:@"stOrderImage.png"]];

	if (self.mDriverInfo.sex == 0) {
		[_imgSex setImage:[UIImage imageNamed:@"icon_male.png"]];
	} else {
		[_imgSex setImage:[UIImage imageNamed:@"icon_female.png"]];
		_lblAge.textColor = MYCOLOR_GREEN;
	}

	[_lblAge setText:[NSString stringWithFormat:@"%d", self.mDriverInfo.age]];
	[_lblName setText:[NSString stringWithFormat:@"%@", self.mDriverInfo.name]];
	[_startToEndAddr setText:[NSString stringWithFormat:@"%@---%@",self.mOrderInfo.startPos,self.mOrderInfo.endPos]];
	[_carNo setText:self.mDriverInfo.carno];
	[_lblDistance setText:self.mOrderInfo.distance_desc];
	[_lblTime setText:self.mOrderInfo.start_time];
	[_lblPassword setText:self.mOrderInfo.password];
}



- (IBAction)onClickedBack:(id)sender
{
//    BACK_VIEW;
    Cus_MainTabViewController *controller = (Cus_MainTabViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"maintab"];
    
    // set target tab
    controller.mCurTab = TAB_ORDER;
    
    SHOW_VIEW(controller);
}



- (IBAction)onClickedMyOrder:(id)sender
{
    Cus_MainTabViewController *controller = (Cus_MainTabViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"maintab"];
    
    // set target tab
    controller.mCurTab = TAB_ORDER;
    
    SHOW_VIEW(controller);
}


@end
